package org.seage.problem.jsp.genetics;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspProblemProvider;

@Testable
public class JspGeneticAlgorithmAdapterTest extends ProblemAlgorithmAdapterTestBase<JspPhenotype> {

  public JspGeneticAlgorithmAdapterTest() {
    super(new JspProblemProvider(), "GeneticAlgorithm");
  }

}
