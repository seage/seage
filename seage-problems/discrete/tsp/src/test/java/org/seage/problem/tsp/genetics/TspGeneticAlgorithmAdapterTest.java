package org.seage.problem.tsp.genetics;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

@Testable
public class TspGeneticAlgorithmAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {

  public TspGeneticAlgorithmAdapterTest() {
    super(new TspProblemProvider(), "GeneticAlgorithm");
  }

}
