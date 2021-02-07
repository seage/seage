package org.seage.launcher.commands;

import java.util.Map;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.IProblemProvider;
import org.seage.hh.experimenter.singlealgorithm.SingleAlgorithmFeedbackExperimenter;


@Parameters(commandDescription = "Perform all single experiment")
public class ExperimentSingleFeedbackAllCommand extends Command {
    @Parameter(names = "-n", required = true, description = "Number of random configs per each experiment")
    int numOfConfigs;
    @Parameter(names = "-t", required = true, description = "Time to run algorithm")
    int algorithmTimeoutS;

    @Override
    public void performCommad() throws Exception {
        Map<String, IProblemProvider<Phenotype<?>>> problemProviders = ProblemProvider.getProblemProviders();
        String[] algorithmIDs = {"GeneticAlgorithm", "TabuSearch", "AntColony", "SimulatedAnnealing"};
        for (String problemID : problemProviders.keySet()) {
            new SingleAlgorithmFeedbackExperimenter(
                problemID, 
                new String[] {"-"},
                algorithmIDs, 
                numOfConfigs, 
                algorithmTimeoutS).runExperiment();
        }
    }
}