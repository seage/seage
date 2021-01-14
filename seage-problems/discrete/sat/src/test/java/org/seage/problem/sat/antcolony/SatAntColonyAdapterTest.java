package org.seage.problem.sat.antcolony;

import org.junit.jupiter.api.Disabled;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class SatAntColonyAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {

  public SatAntColonyAdapterTest() {
    super(new SatProblemProvider(), "AntColony");
  }

}
