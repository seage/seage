package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

import org.seage.hh.experimenter.singlealgorithm.SingleAlgorithmFeedbackExperimenter;

@Parameters(commandDescription = "Perform single feedback experiment")
public class ExperimentSingleFeedbackCommand extends Command {
  @Parameter(names = "-p", required = true, description = "ProblemID")
  String problemID;
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

  @Override
  public void performCommand() throws Exception {
    new SingleAlgorithmFeedbackExperimenter(problemID, instances.toArray(new String[] {}),
        algorithms.toArray(new String[] {}), numOfConfigs, algorithmTimeoutS).runExperiment();
  }
}
