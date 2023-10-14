package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.seage.hh.experimenter.singlealgorithm.SingleAlgorithmFeedbackExperiment;

@Parameters(commandDescription = "Perform single feedback experiment")
public class ExperimentSingleFeedbackCommand extends Command {
  @Parameter(
      names = "-i", required = true,
      description = "Problem instances  [PROBLEM_DOMAIN:instance]", 
      variableArity = true
  )
  List<String> instances;
  
  @Parameter(names = "-a", required = true, description = "Algorithms to run", variableArity = true)
  List<String> algorithms;
  
  @Parameter(names = "-n", required = true, description = "Number of configs per each experiment")
  int numOfConfigs;
  
  @Parameter(names = "-t", required = true, description = "Time to run algorithm")
  int algorithmTimeoutS;
  
  @Parameter(names = "-s", required = false, description = "Spread around the parameter value")
  double spread = 0.1;
  
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
        new SingleAlgorithmFeedbackExperiment(algorithmID, problemID, instanceIDs, numOfConfigs,
            algorithmTimeoutS, spread, tag).run();
      }
    }
  }
}
