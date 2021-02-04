package org.seage.problem.sat.sannealing;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

public class SatSimulatedAnnealingAdapterTest 
    extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {

  public SatSimulatedAnnealingAdapterTest() {
    super(new SatProblemProvider(), "SimulatedAnnealing");
  }
}
