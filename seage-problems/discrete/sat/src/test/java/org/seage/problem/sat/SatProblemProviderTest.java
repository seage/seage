package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemProviderTestBase;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;

public class SatProblemProviderTest {
  SatProblemProvider problemProvider;

  @BeforeAll
  public void init() {
    problemProvider = new SatProblemProvider();
  }

  @Test
  public void generateGreedySolutionTest() throws Exception {
    Random rnd = new Random(10000);
    
    IProblemProvider provider = ProblemProvider.getProblemProviders().get("SAT");
    // problem instance
    ProblemInstance instance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf20-01"));

        
    assertNotNull(problemProvider.generateGreedySolution(instance, rnd.nextLong()));
  }
}
