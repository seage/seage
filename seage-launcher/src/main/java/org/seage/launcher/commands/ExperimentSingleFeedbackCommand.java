package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;
import java.util.Map;
import org.seage.hh.experimenter.Experimenter;

@Parameters(commandDescription = "Perform single feedback experiment")
public class ExperimentSingleFeedbackCommand extends Command {
  @Parameter(
      names = "-i", required = true,
      description = "Problem instances  [PROBLEM_DOMAIN:instance]", 
      variableArity = true
  )
  List<String> instances;
  @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
  List<String> algorithms;
  @Parameter(names = "-n", required = true, description = "Number of configs per each experiment")
  int numOfConfigs;
  @Parameter(names = "-t", required = true, description = "Time to run algorithm")
  int algorithmTimeoutS;
  @Parameter(names = "-s", required = true, description = "Spread around the parameter value")
  double spread;

  @Override
  public void performCommand() throws Exception {
    Map<String, List<String>> problemInstanceParams = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instances);
    for (String algorithmID : algorithms) {
      new Experimenter(
        algorithmID, problemInstanceParams, numOfConfigs, 
        algorithmTimeoutS).setSpread(spread).runExperiment("SingleAlgorithmFeedback");
    }
  }
}
