package org.seage.problem.jssp.genetics;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.jssp.JsspPhenotype;
import org.seage.problem.jssp.JsspProblemProvider;

@Testable
public class JsspGeneticAlgorithmAdapterTest extends ProblemAlgorithmAdapterTestBase<JsspPhenotype> {

  public JsspGeneticAlgorithmAdapterTest() {
    super(new JsspProblemProvider(), "GeneticAlgorithm");
  }

}
