package org.seage.problem.jsp.antcolony;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspProblemProvider;

@Testable
public class JspAntColonyAdapterTest extends ProblemAlgorithmAdapterTestBase<JspPhenotype> {

  public JspAntColonyAdapterTest() {
    super(new JspProblemProvider(), "AntColony");
  }
}
