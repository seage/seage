package org.seage.problem.sat;

import org.seage.aal.problem.ProblemProviderTestBase;

public class SatProblemProviderTest extends ProblemProviderTestBase<SatPhenotype> {
  public SatProblemProviderTest() {
    super(new SatProblemProvider());
  }
}
