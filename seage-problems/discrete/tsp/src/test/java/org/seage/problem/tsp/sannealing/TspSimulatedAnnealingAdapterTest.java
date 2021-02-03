package org.seage.problem.tsp.sannealing;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

// @Disabled("Adapter class not fully implemented yet")
public class TspSimulatedAnnealingAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {
  public TspSimulatedAnnealingAdapterTest() {
    super(new TspProblemProvider(), "SimulatedAnnealing");
  }
}
