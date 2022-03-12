package org.seage.launcher.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import java.util.Map;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemMetadataGenerator;
import org.seage.aal.problem.ProblemProvider;

@Parameters(commandDescription = "Generate metadata for a given problem")
public class MetadataGeneratorCommand extends Command {
  @Parameter(names = "-p", required = true, description = "ProblemID")
  String problemID;
  @Parameter(names = "-n", required = true, description = "Number of trials")
  int numberOfTrials;

  @Override
  public void performCommand() throws Exception {
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    IProblemProvider<Phenotype<?>> provider = providers.get(problemID);
    ProblemMetadataGenerator<?> generator = provider.initProblemMetadataGenerator();
    generator.runMetadataGenerator(numberOfTrials);
  }
}