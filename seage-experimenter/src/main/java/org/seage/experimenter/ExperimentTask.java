/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.experimenter;

import java.io.File;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
class ExperimentTask implements Runnable{
    private ProblemConfig _config;
    private long _experimentID;
    private long _runID;
    private long _timeout = 9000;
    private static long _runOrder=100000;

    public ExperimentTask(long experimentID, long runID, long timeoutS, ProblemConfig config){
        _experimentID = experimentID;
        _runID = runID;
        _config = config;
        _timeout = timeoutS*1000;
    }
    
    public void run() {
        String problemID = "";
        String algorithmID = "";
        String instanceName = "";
        try{
            problemID = _config.getDataNode("Problem").getValueStr("id");
            algorithmID = _config.getDataNode("Algorithm").getValueStr("id");

            // provider and factory
            IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
            IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

            // problem instance
            ProblemInstanceInfo instance = provider.initProblemInstance(_config);
            instanceName = instance.toString();
            // algorithm
            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, _config);

            AlgorithmParams algNode = _config.getAlgorithmParams();
            Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance);

            algorithm.solutionsFromPhenotype(solutions);
            algorithm.setParameters(algNode);
            algorithm.startSearching(true);
            waitForTimeout(algorithm);
            algorithm.stopSearching();
            
            solutions = algorithm.solutionsToPhenotype();

            // phenotype evaluator
            IPhenotypeEvaluator evaluator = provider.initPhenotypeEvaluator();
            double[] result = evaluator.evaluate(solutions[0], instance);

            AlgorithmReport algReport = algorithm.getReport();
            
            DataNode expReport = new DataNode("ExperimentTask");
            expReport.putValue("experimentID", _experimentID);
            expReport.putValue("runID", _runID);
            expReport.putDataNode(algReport);
            expReport.putDataNode(_config);
            
            //String experimentID = _config.getValueStr("experimentID");
            String configID = _config.getValueStr("configID");
                       
            File dir = new File("output/"+_experimentID);
            if(!dir.exists()) dir.mkdirs();

            String path = dir.getPath()+"/"+problemID +"-"+instanceName.split("\\.")[0] +"-"+algorithmID+"-"+getRunOrder()+"-"+_experimentID+".xml";
            XmlHelper.writeXml(expReport, path);

            System.out.printf("%s %15s\t %20s\t %20s\n", algorithmID, instance.toString(), result[0], configID);
        }
        catch(Exception ex){
            System.err.println("ERR: " + problemID +"/"+algorithmID+"/"+instanceName);
            System.err.println(_config.toString());
            ex.printStackTrace();
        }
    }
    
    private void waitForTimeout(IAlgorithmAdapter alg) throws Exception
    {
        long time = System.currentTimeMillis();
        while(alg.isRunning() && ((System.currentTimeMillis()-time)<_timeout))
            Thread.sleep(300);
    }
    
    synchronized private static long getRunOrder()
    {
        return _runOrder++;
    }

}
