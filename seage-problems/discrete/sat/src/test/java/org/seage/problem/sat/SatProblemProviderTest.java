package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemProviderTestBase;

public class SatProblemProviderTest extends ProblemProviderTestBase<SatPhenotype> {
  public SatProblemProviderTest() {
    super(new SatProblemProvider());
  }

  static SatProblemProvider problemProvider;

  @BeforeAll
  public static void init() {
    problemProvider = new SatProblemProvider();
  }

  @Test
  public void generateGreedySolutionTest() throws Exception {
    Random rnd = new Random();
    ProblemProvider.registerProblemProviders(new Class<?>[] { SatProblemProvider.class });
    IProblemProvider<Phenotype<?>> provider = ProblemProvider.getProblemProviders().get("SAT");
    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf250-01"));

    SatPhenotype solution = problemProvider.generateGreedySolution(instance, rnd.nextLong());
    assertNotNull(solution);
  }

  @Test
  public void generateGreedySolutionsTest() throws Exception {
    Random rnd = new Random();
    ProblemProvider.registerProblemProviders(new Class<?>[] { SatProblemProvider.class });
    IProblemProvider<Phenotype<?>> provider = ProblemProvider.getProblemProviders().get("SAT");
    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf250-01"));

    SatPhenotype[] solutions = 
      problemProvider.generateGreedySolutions(instance, 9, rnd.nextLong());   
    assertNotNull(solutions);
  }
}
