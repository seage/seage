package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.util.List;
import java.util.Map;
import org.seage.hh.experimenter.Experimenter;

@Parameters(commandDescription = "Perform Experiment Approach")
public class ExperimentApproachCommand extends Command {

  @Parameter(
      names = {"-i", "--instance"}, 
      required = true, 
      description = "Problem instances [PROBLEM_DOMAIN:instance]",
      variableArity = true
  )
  List<String> instanceIDs;

  @Parameter(
      names = {"-a", "--algorithmID"}, 
      required = true, 
      description = "Algorithm ID", 
      variableArity = false
  )
  String algorithmID;
  
  @Parameter(
      names = "-n", 
      required = true, 
      description = "Number of runs per instance"
  )
  int runsPerInstance;
  
  @Parameter(
      names = "-t", 
      required = true, 
      description = "Time to run algorithm"
  )
  int algorithmTimeoutS;

  @Parameter(names = {"-T", "--tag"}, required = false, description = "Tag to mark the experiment")
  String tag;

  @Override
  public void performCommand() throws Exception {
    Map<String, List<String>> problemInstanceParams = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instanceIDs);

    // new Experimenter(
    //     algorithmID, problemInstanceParams, runsPerInstance, algorithmTimeoutS, tag).runExperiment();
    throw new UnsupportedOperationException("This has to be fixed or removed");
  }
}
