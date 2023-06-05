package org.seage.problem.fsp.sannealing;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.fsp.FspProblemProvider;
import org.seage.problem.jsp.JspPhenotype;

/**
 * .
 */
@Testable
public class FspSimulatedAnnealingFactoryTest 
    extends ProblemAlgorithmAdapterTestBase<JspPhenotype> {
  public FspSimulatedAnnealingFactoryTest() {
    super(new FspProblemProvider(), "SimulatedAnnealing");
  }
}
