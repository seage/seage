/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import org.seage.aal.AlgorithmReport;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemConfig;
import org.seage.aal.ProblemInstance;
import org.seage.aal.ProblemProvider;
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

            DataNode algNode = _config.getDataNode("Algorithm");
            Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance);

            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching(algNode.getDataNode("Parameters"));
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
