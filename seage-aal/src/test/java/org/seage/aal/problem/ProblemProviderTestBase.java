package org.seage.aal.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;

public abstract class ProblemProviderTestBase<P extends Phenotype<?>> {
  // protected IAlgorithmFactory<P, ?> _algorithmFactory;
  protected IProblemProvider<P> problemProvider;
  protected List<ProblemInstanceInfo> problemInstanceInfos;

  /**. */
  public ProblemProviderTestBase(IProblemProvider<P> problemProvider) {
    try {
      this.problemProvider = problemProvider;
      assertNotNull(problemProvider);
      assertNotNull(problemProvider.getProblemInfo());
    } catch (Exception e) {
      fail(e);
    }
  }

  /**. */
  @BeforeEach
  public void setUp() {
    try {
      ProblemInfo pi = problemProvider.getProblemInfo();
      assertNotNull(pi.getProblemInstanceInfos());
      this.problemInstanceInfos = pi.getProblemInstanceInfos();
      assertNotNull(problemInstanceInfos);
      assertFalse(problemInstanceInfos.isEmpty(), "Can not get list of instances");
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testProblemInfo() throws Exception {
    ProblemInfo pi = problemProvider.getProblemInfo();
    assertNotNull(pi);
    assertEquals(pi.getDataNode("Algorithms").getDataNodes("Algorithm").size(),
        problemProvider.getAlgorithmFactories().values().size());
  }

  @Test
  public void testInitProblemInstances() throws Exception {
    for (ProblemInstanceInfo pii : problemInstanceInfos) {
      try {
        ProblemInstance pin = problemProvider.initProblemInstance(pii);
        assertNotNull(pin);
      } catch (Exception ex) {
        Assertions.fail("Fail to read " + pii.getInstanceID(), ex);
      }
    }
  }


  @Test
  public void testPhenotypeEvaluator() throws Exception {
    ProblemInstance pin = problemProvider.initProblemInstance(problemInstanceInfos.get(0));
    IPhenotypeEvaluator<P> phenotypeEvaluator = problemProvider.initPhenotypeEvaluator(pin);
    assertNotNull(phenotypeEvaluator);
    P[] solutions = problemProvider.generateInitialSolutions(pin, 10, 10);

    for (int i = 0; i < 10; i++) {
      double[] value = phenotypeEvaluator.evaluate(solutions[i]);
      assertNotNull(value);
    }
  }
}
