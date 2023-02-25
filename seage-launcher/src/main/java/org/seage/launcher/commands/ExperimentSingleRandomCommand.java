package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.List;
import java.util.Map;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.singlealgorithm.SingleAlgorithmRandomExperiment;

@Parameters(commandDescription = "Perform single random experiment")
public class ExperimentSingleRandomCommand extends Command {
  @Parameter(
      names = "-i",
      required = true,
      description = "Problem instances [PROBLEM_DOMAIN:instance]",
      variableArity = true
  )
  List<String> instances;
  
  @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
  List<String> algorithms;
  
  @Parameter(
      names = "-n", required = true, description = "Number of random configs per each experiment")
  int numOfConfigs;
  
  @Parameter(names = "-t", required = true, description = "Time to run algorithm")
  int algorithmTimeoutS;

  @Parameter(names = {"-T", "--tag"}, required = false, description = "Tag to mark the experiment")
  String tag;

  @Override
  public void performCommand() throws Exception {
    Map<String, List<String>> problemInstanceParams = 
        ProblemInstanceParamsParser.parseProblemInstanceParams(instances);
    for (String algorithmID : algorithms) {
      new Experimenter(
        algorithmID, problemInstanceParams, numOfConfigs, 
        algorithmTimeoutS, tag).runExperiment("SingleAlgorithmRandom");
    }
  }
}