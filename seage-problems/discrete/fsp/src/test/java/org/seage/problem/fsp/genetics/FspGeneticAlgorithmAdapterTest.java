package org.seage.problem.fsp.genetics;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.fsp.FspProblemProvider;

@Testable
public class FspGeneticAlgorithmAdapterTest extends ProblemAlgorithmAdapterTestBase<JspPhenotype> {

  public FspGeneticAlgorithmAdapterTest() {
    super(new FspProblemProvider(), "GeneticAlgorithm");
  }

}
