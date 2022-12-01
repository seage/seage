package org.seage.problem.sat.antcolony;

import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

@Testable
public class SatAntColonyAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {

  public SatAntColonyAdapterTest() {
    super(new SatProblemProvider(), "AntColony");
  }

}
