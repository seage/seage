package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemProvider;

public class SatInitialSolutionProviderTest {
  @Test
  public void generateGreedySolutionTest() throws Exception {
    ProblemProvider.registerProblemProviders(new Class<?>[] { SatProblemProvider.class });
    SatProblemProvider provider = new SatProblemProvider();
    // problem instance
    ProblemInstance problemInstance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf250-01"));

    Formula formula = (Formula) problemInstance;
    SatProblemProvider satProblemProvider = new SatProblemProvider();
    IPhenotypeEvaluator<SatPhenotype> evaluator = 
        satProblemProvider.initPhenotypeEvaluator(problemInstance);

            
    SatPhenotype solution = 
        SatInitialSolutionProvider
        .generateGreedySolution(formula, evaluator, System.currentTimeMillis());
    assertNotNull(solution);
  }

  @Test
  public void generateGreedySolutionsTest() throws Exception {
    ProblemProvider.registerProblemProviders(new Class<?>[] { SatProblemProvider.class });
    SatProblemProvider provider = new SatProblemProvider();
    // problem instance
    ProblemInstance problemInstance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf250-01"));

    Formula formula = (Formula) problemInstance;
    SatProblemProvider satProblemProvider = new SatProblemProvider();
    IPhenotypeEvaluator<SatPhenotype> evaluator = 
        satProblemProvider.initPhenotypeEvaluator(problemInstance);
            
    SatPhenotype[] solutions = 
        SatInitialSolutionProvider
        .generateGreedySolutions(formula, evaluator, 9, System.currentTimeMillis());   
    assertNotNull(solutions);
  }
}
