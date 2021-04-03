package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;

@Parameters(commandDescription = "Perform Experiment Approach")
public class ExperimentApproach extends Command {
  @Parameter(names = "-p", required = true, description = "ProblemID")
  String problemID;
  
  @Parameter(
      names = "--instance", 
      required = true, 
      description = "Problem instances", 
      variableArity = true
  )
  List<String> instances;

  @Parameter(
      names = "--algorithmID", 
      required = true, 
      description = "Algorithms IDs", 
      variableArity = true
  )
  List<String> algorithms;
  
  @Parameter(
      names = "-n", 
      required = true, 
      description = "Number of random configs per each experiment"
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
    
  }
}
