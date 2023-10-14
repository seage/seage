package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.seage.hh.experiment.singlealgorithm.SingleAlgorithmDefaultExperiment;
import org.seage.hh.experiment.singlealgorithm.evolution.SingleAlgorithmConfigsEvolutionExperiment;

@Parameters(commandDescription = "Perform single evolution experiment")
public class ExperimentSingleEvolutionCommand extends Command {
  @Parameter(names = "-i", required = true,
      description = "Problem instances [PROBLEM_DOMAIN:instance]", variableArity = true)
  List<String> instances;

  @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
  List<String> algorithms;

  @Parameter(names = "-n", required = true,
      description = "Number of config subject in a generation ")
  int numSubjects;

  @Parameter(names = "-g", required = true, description = "Number of GA generations")
  int numGenerations;

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
        new SingleAlgorithmConfigsEvolutionExperiment(
            algorithmID, problemID, instanceIDs, 
            numSubjects, numGenerations, algorithmTimeoutS, tag).run();
      }
    }
  }
}
