/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    ProblemConfig _config;

    public ExperimentTask(ProblemConfig config){
        _config = config;
    }
    
    public void run() {
        try{
            String problemID = _config.getDataNode("Problem").getValueStr("id");
            String algorithmID = _config.getDataNode("Algorithm").getValueStr("id");

            // provider and factory
            IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
            IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

            // problem instance
            ProblemInstance instance = provider.initProblemInstance(_config);

            // algorithm
            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, _config);

            AlgorithmParams algNode = _config.getAlgorithmParams();
            Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance);

            algorithm.solutionsFromPhenotype(solutions);
            algorithm.setParameters(algNode);
            algorithm.startSearching();
            solutions = algorithm.solutionsToPhenotype();

            // phenotype evaluator
            IPhenotypeEvaluator evaluator = provider.initPhenotypeEvaluator();
            double[] result = evaluator.evaluate(solutions[0], instance);

            AlgorithmReport report = algorithm.getReport();
            XmlHelper.writeXml(report, "output/"+System.currentTimeMillis()+".xml");

            System.out.printf("%s %15s\t %s\n", algorithmID, instance.toString(), result[0]);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
