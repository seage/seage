package org.seage.launcher.commands;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.seage.hh.experiment.singlealgorithm.SingleAlgorithmGridExperiment;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Perform single random experiment")
public class ExperimentSingleGridCommand extends Command {
  @Parameter(names = "-i", required = true,
      description = "Problem instances [PROBLEM_DOMAIN:instance]", variableArity = true)
  List<String> instances;

  @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
  List<String> algorithms;

  @Parameter(names = "-n", required = true,
      description = "Number of random configs per each experiment")
  int numOfConfigs;

  @Parameter(names = "-t", required = true, description = "Time to run algorithm")
  int algorithmTimeoutS;

  @Parameter(names = "-g", required = true,
      description = "Granularity of the parameter values grid: G x G x ... x G")
  int granularity;

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
        new SingleAlgorithmGridExperiment(algorithmID, problemID, instanceIDs, numOfConfigs,
            algorithmTimeoutS, granularity, tag).run();
      }
    }
  }
}
