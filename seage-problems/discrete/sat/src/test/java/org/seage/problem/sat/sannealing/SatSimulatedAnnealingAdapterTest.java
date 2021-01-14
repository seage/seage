package org.seage.problem.sat.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class SatSimulatedAnnealingAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {

  public SatSimulatedAnnealingAdapterTest() {
    super(new SatProblemProvider(), "SimulatedAnnealing");
  }
}
