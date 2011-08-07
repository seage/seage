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

import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.reporting.AlgorithmReport;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.algorithm.ProblemInstance;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
class ExperimentTask implements Runnable{
    private ProblemConfig _config;
    private long _timeout = 9000;

    public ExperimentTask(ProblemConfig config){
        _config = config;
    }
    
    public void run() {
        String problemID = "";
        String algorithmID = "";
        String InstanceName = "";
        try{
            problemID = _config.getDataNode("Problem").getValueStr("id");
            algorithmID = _config.getDataNode("Algorithm").getValueStr("id");

            // provider and factory
            IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
            IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

            // problem instance
            ProblemInstance instance = provider.initProblemInstance(_config);
            InstanceName = instance.toString();
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
            
            DataNode expReport = new DataNode("ExperimentReport");             
            expReport.putDataNode(algReport);
            expReport.putDataNode(_config);
            
            String runID = _config.getValueStr("runID");
            String configID = _config.getValueStr("configID");
            //report.putValue("runID", runID);
            //report.putValue("configID", configID);
            String path = "output/"+runID+"-"+problemID +"-"+InstanceName.split("\\.")[0] +"-"+algorithmID+"-"+System.currentTimeMillis()+".xml";
            XmlHelper.writeXml(expReport, path);

            System.out.printf("%s %15s\t %20s\t %20s\n", algorithmID, instance.toString(), result[0], configID);
        }
        catch(Exception ex){
            System.err.println("ERR: " + problemID +"/"+algorithmID+"/"+InstanceName);
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

}
