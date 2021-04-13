package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;

public class SatProblemProviderTest {
  static SatProblemProvider problemProvider;

  @BeforeAll
  public static void init() {
    problemProvider = new SatProblemProvider();
  }

  @Test
  public void generateGreedySolutionTest() throws Exception {
    Random rnd = new Random(10000);

    ProblemProvider.registerProblemProviders(new Class<?>[] { SatProblemProvider.class });
    IProblemProvider<Phenotype<?>> provider = ProblemProvider.getProblemProviders().get("SAT");
    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf20-01"));

        
    assertNotNull(problemProvider.generateGreedySolution(instance, rnd.nextLong()));
  }
}
