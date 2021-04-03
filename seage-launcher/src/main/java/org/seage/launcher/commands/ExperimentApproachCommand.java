package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.HashMap;
import java.util.List;

import org.seage.hh.experimenter2.ApproachExperimenter;

@Parameters(commandDescription = "Perform Experiment Approach")
public class ExperimentApproachCommand extends Command {

  @Parameter(
      names = {"-i", "--instance"}, 
      required = true, 
      description = "Problem instances",
      variableArity = true
  )
  List<String> instanceIDs;

  @Parameter(
      names = {"-a", "--algorithmID"}, 
      required = true, 
      description = "Algorithm ID", 
      variableArity = false
  )
  List<String> algorithmIDs;
  
  @Parameter(
      names = "-n", 
      required = true, 
      description = "Number of configs per each experiment"
  )
  int numOfConfigs;
  
  @Parameter(
      names = "-t", 
      required = true, 
      description = "Time to run algorithm"
  )
  int algorithmTimeoutS;

  @Override
  public void performCommad() throws Exception {
    HashMap<String, List<String>> problemInstanceParams = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceIDs);

    for (String algorithmID: algorithmIDs) {
      new ApproachExperimenter(algorithmID, problemInstanceParams);
    }
  }
}
