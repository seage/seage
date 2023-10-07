package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.List;
import java.util.Map;
import org.seage.hh.experimenter.Experimenter;

@Parameters(commandDescription = "Perform single evolution experiment")
public class ExperimentSingleEvolutionCommand extends Command {
  @Parameter(names = "-i", required = true,
      description = "Problem instances [PROBLEM_DOMAIN:instance]", variableArity = true)
  List<String> instances;

  @Parameter(names = "-a", required = true, description = "Algorithms", variableArity = true)
  List<String> algorithms;

  @Parameter(names = "-n", required = true,
      description = "Number of configs per experiment")
  int numOfSubjects;


  @Parameter(names = "-g", required = true, description = "Number of iterations")
  int numOfIterations;

  @Parameter(names = "-t", required = true, description = "Time to run algorithm")
  int algorithmTimeoutS;

  @Parameter(names = {"-T", "--tag"}, required = false, description = "Tag to mark the experiment")
  String tag;

  @Override
  public void performCommand() throws Exception {
    // new SingleAlgorithmEvolutionExperiment(
    // problemID, instances.toArray(new String[]{}),
    // algorithms.toArray(new String[]{}), numOfSubjects, numOfIterations, algorithmTimeoutS )
    // .runExperiment();
    Map<String, List<String>> problemInstanceParams =
        ProblemInstanceParamsParser.parseProblemInstanceParams(instances);

    for (String algorithmID : algorithms) {
      new Experimenter(algorithmID, problemInstanceParams, numOfSubjects, algorithmTimeoutS, tag)
          .setNumOfIterations(numOfIterations).runExperiment("SingleAlgorithmEvolution");
    }
  }
}
