package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.singlealgorithm.evolution.SingleAlgorithmEvolutionExperiment;

@Parameters(commandDescription = "Perform single evolution experiment")
public class ExperimentSingleEvolutionCommand extends Command {
  @Parameter(names = "-i", required = true,
      description = "Problem instances [PROBLEM_DOMAIN:instance]", variableArity = true)
  List<String> instances;

  @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
  List<String> algorithms;

  @Parameter(names = "-n", required = true,
      description = "Number of random configs per each experiment")
  int numOfSubjects;

  @Parameter(names = "-g", required = true, description = "Number of iterations")
  int numOfIterations;

  @Parameter(names = "-t", required = true, description = "Time to run algorithm")
  int algorithmTimeoutS;

  @Parameter(names = {"-T", "--tag"}, required = false, description = "Tag to mark the experiment")
  String tag;

  @Override
  public void performCommand() throws Exception {
    Map<String, List<String>> problemInstanceParams =
        ProblemInstanceParamsParser.parseProblemInstanceParams(instances);

    Set<String> problemIDs = problemInstanceParams.keySet();

    for (String problemID : problemIDs) {
      List<String> instanceIDs = problemInstanceParams.get(problemID);
      for (String algorithmID : algorithms) {
        new SingleAlgorithmEvolutionExperiment(algorithmID, problemID, instanceIDs, numOfSubjects,
            numOfIterations, algorithmTimeoutS, tag).run();
      }
    }
  }
}
