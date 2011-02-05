/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemConfig;
import org.seage.aal.ProblemInstance;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;

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


            System.out.printf("%s: %4s %s\n", "Problem","", problemID);
            System.out.printf("%s: %2s %s\n", "Algorithm","", algorithmID);
            System.out.printf("%s: %3s %s\n", "Instance","", instance);
            System.out.println("Running ...");
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.startSearching(algNode.getDataNode("Parameters"));
            solutions = algorithm.solutionsToPhenotype();

            // phenotype evaluator
            IPhenotypeEvaluator evaluator = provider.initPhenotypeEvaluator();
            double[] result = evaluator.evaluate(solutions[0], instance);

            System.out.printf("%s: %5s %s\n", "Result","", result[0]);
            //System.out.println(": " + result[0]);

            System.out.printf("%s: %3s ", "Solution","");
            for(int i=0;i<solutions[0].length;i++)
                System.out.print(solutions[0][i]+" ");
            System.out.println();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
