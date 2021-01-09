package org.seage.aal.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.seage.data.ObjectCloner;

public abstract class ProblemProviderTestBase<P extends Phenotype<?>> {
  // protected IAlgorithmFactory<P, ?> _algorithmFactory;
  protected IProblemProvider<P> _problemProvider;
  protected ProblemInstanceInfo _problemInstanceInfo;

  public ProblemProviderTestBase(IProblemProvider<P> problemProvider) {
    try {
      _problemProvider = problemProvider;
      assertNotNull(_problemProvider);
      assertNotNull(_problemProvider.getProblemInfo());
    } catch (Exception e) {
      fail(e);
    }
  }

  @BeforeEach
  public void setUp() {
    try {
      ProblemInfo pi = _problemProvider.getProblemInfo();
      assertNotNull(pi.getProblemInstanceInfos());
      _problemInstanceInfo = pi.getProblemInstanceInfos().get(0);
      assertNotNull(_problemInstanceInfo);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
  public void testProblemInfo() throws Exception {
    ProblemInfo pi = _problemProvider.getProblemInfo();
    assertNotNull(pi);
    assertEquals(pi.getDataNode("Algorithms").getDataNodes("Algorithm").size(),
        _problemProvider.getAlgorithmFactories().values().size());
  }

  @Test
  public void testInitProblemInstance() throws Exception {
    ProblemInstance pin = _problemProvider.initProblemInstance(_problemInstanceInfo);
    assertNotNull(pin);
  }

  @Test
  public void testPhenotypeEvaluator() throws Exception {
    ProblemInstance pin = _problemProvider.initProblemInstance(_problemInstanceInfo);
    IPhenotypeEvaluator<P> phenotypeEvaluator = _problemProvider.initPhenotypeEvaluator(pin);
    assertNotNull(phenotypeEvaluator);
    P[] solutions = _problemProvider.generateInitialSolutions(pin, 10, 10);

    for (int i = 0; i < 10; i++) {
      double[] value = phenotypeEvaluator.evaluate(solutions[i]);
      assertNotNull(value);
    }
  }
}
