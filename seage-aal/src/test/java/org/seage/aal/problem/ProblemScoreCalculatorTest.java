package org.seage.aal.problem;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import org.seage.aal.algorithm.Phenotype;

public class ProblemScoreCalculatorTest {
  protected ProblemScoreCalculator problemScoreCalculator;

  @Test
  public void testCalculatingOptimalInstanceScore() throws Exception {
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    System.out.println(providers.keySet());
    IProblemProvider<?> pp = providers.get("SAT");
    ProblemInfo problemInfo = pp.getProblemInfo();

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double optimum = problemInfo.getProblemInstanceInfo("uf100-0169").getValueDouble("optimum");
   
    assertEquals(1.0, problemScoreCalculator.calculateInstanceScore("uf100-169", optimum), 0.001);
  }


  @Test
  public void testCalculatingRandomInstanceScore() throws Exception {
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    IProblemProvider<?> pp = providers.get("SAT");
    ProblemInfo problemInfo = pp.getProblemInfo();

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double random = problemInfo.getProblemInstanceInfo("uf100-0169").getValueDouble("random");
    
    assertEquals(0.0, problemScoreCalculator.calculateInstanceScore("uf100-169", random), 0.001);
  }

  @Test
  public void testCalculatingMidsectionInstanceScore() throws Exception {
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    IProblemProvider<?> pp = providers.get("SAT");
    ProblemInfo problemInfo = pp.getProblemInfo();

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double optimum = problemInfo.getProblemInstanceInfo("uf100-0169").getValueDouble("optimum");
    double random = problemInfo.getProblemInstanceInfo("uf100-0169").getValueDouble("random");
    double midsection = random - optimum;

    assertEquals(0.5, 
        problemScoreCalculator.calculateInstanceScore("uf100-169", midsection), 0.001);
  }

  @Test
  public void testCalculatingBetterThanOptimumInstanceScore() throws Exception {
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    IProblemProvider<?> pp = providers.get("SAT");
    ProblemInfo problemInfo = pp.getProblemInfo();

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double optimum = problemInfo.getProblemInstanceInfo("uf100-0169").getValueDouble("optimum");
    
    assertThrows(
        Exception.class, 
        () -> problemScoreCalculator.calculateInstanceScore("uf100-169", optimum - 1));
  }

  @Test
  public void testCalculatingWorseThanRandomInstanceScore() throws Exception {
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    IProblemProvider<?> pp = providers.get("SAT");
    ProblemInfo problemInfo = pp.getProblemInfo();

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    double random = problemInfo.getProblemInstanceInfo("uf100-0169").getValueDouble("random");
  
    assertEquals(0.0, random + 1);
  }

  @Test
  public void testCalculatingProblemScore() throws Exception {
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();
    IProblemProvider<?> pp = providers.get("SAT");
    ProblemInfo problemInfo = pp.getProblemInfo();

    problemScoreCalculator = new ProblemScoreCalculator(problemInfo);
    
    String[] names = {"1", "2", "3", "4", "5"};
    double[] values = {0.4, 0.2, 0.1, 0.6, 0.3};
    double meanValue = Arrays.stream(values).sum() / values.length;

    assertEquals(meanValue, problemScoreCalculator.calculateProblemScore(names, values), 0.001);
  }
}
