package org.seage.problem.sat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;

public class SatInitialSolutionProviderTest {
  @Test
  public void generateGreedySolutionTest() throws Exception {
    SatProblemProvider provider = new SatProblemProvider();
    ProblemInstance problemInstance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf100-01"));

    Formula formula = (Formula) problemInstance;
    IPhenotypeEvaluator<SatPhenotype> evaluator = provider.initPhenotypeEvaluator(problemInstance);
            
    SatPhenotype solution = SatInitialSolutionProvider
        .generateGreedySolution(formula, evaluator, System.currentTimeMillis());
    assertNotNull(solution);
    assertNotNull(solution.getObjValue());
    assertNotNull(solution.getScore());
  }

  @Test
  public void generateGreedySolutionsTest() throws Exception {
    SatProblemProvider provider = new SatProblemProvider();
    ProblemInstance problemInstance = provider
        .initProblemInstance(provider.getProblemInfo().getProblemInstanceInfo("uf100-01"));

    Formula formula = (Formula) problemInstance;
    IPhenotypeEvaluator<SatPhenotype> evaluator = provider.initPhenotypeEvaluator(problemInstance);
            
    SatPhenotype[] solutions = SatInitialSolutionProvider
        .generateGreedySolutions(formula, evaluator, 3, System.currentTimeMillis());   
    assertNotNull(solutions);
    assertEquals(3, solutions.length);
  }
}
