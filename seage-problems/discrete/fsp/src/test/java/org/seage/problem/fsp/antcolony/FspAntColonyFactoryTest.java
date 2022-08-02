package org.seage.problem.fsp.antcolony;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jsp.JspPhenotype;

@Testable
public class FspAntColonyFactoryTest extends ProblemAlgorithmAdapterTestBase<JspPhenotype> {
  public FspAntColonyFactoryTest() {
    super(new FspProblemProvider(), "AntColony");
  }
}
