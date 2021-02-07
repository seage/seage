package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.List;
import org.seage.hh.experimenter.singlealgorithm.SingleAlgorithmDefaultExperimenter;

@Parameters(commandDescription = "Perform single random experiment")
public class ExperimentSingleDefaultCommand extends Command {
  @Parameter(names = "-p", required = true, description = "ProblemID")
  String problemID;
  @Parameter(names = "-i", required = true, description = "Problem instances", variableArity = true)
  List<String> instances;
  @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
  List<String> algorithms;
  @Parameter(names = "-n", required = true, description = "Number of random configs per each experiment")
  int numOfConfigs;
  @Parameter(names = "-t", required = true, description = "Time to run algorithm")
  int algorithmTimeoutS;
  @Parameter(names = "-s", required = true, description = "Spread around the default parameter value")
  double spread;

  @Override
  public void performCommad() throws Exception {
    new SingleAlgorithmDefaultExperimenter(
        problemID, 
        instances.toArray(new String[] {}),
        algorithms.toArray(new String[] {}), 
        numOfConfigs, 
        algorithmTimeoutS,
        spread).runExperiment();
  }
}