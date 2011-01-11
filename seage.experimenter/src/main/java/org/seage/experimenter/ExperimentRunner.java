/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.experimenter;

import java.io.File;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IPhenotypeEvaluator;
import org.seage.aal.IProblemProvider;
import org.seage.aal.ProblemInstance;
import org.seage.aal.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 *
 * @author rick
 */
public class ExperimentRunner {

    public void run(String configPath) throws Exception
    {
        DataNode config = XmlHelper.readXml(new File(configPath));

        String problemID = config.getDataNode("Problem").getValueStr("id");
        String algorithmID = config.getDataNode("Algorithm").getValueStr("id");

        System.out.println("Running "+algorithmID+" algorithm on "+problemID+" problem ...");

        // provider and factory
        IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
        IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

        // problem instance
        ProblemInstance instance = provider.initProblemInstance(config);

        // algorithm
        IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, config);

        DataNode algNode = config.getDataNode("Algorithm");
        Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance);

        algorithm.solutionsFromPhenotype(solutions);
        algorithm.startSearching(algNode.getDataNode("Parameters"));
        solutions = algorithm.solutionsToPhenotype();

        // phenotype evaluator
        IPhenotypeEvaluator evaluator = provider.initPhenotypeEvaluator();
        double[] result = evaluator.evaluate(solutions[0], instance);

        System.out.println("Result: " + result[0]);
    }
}
