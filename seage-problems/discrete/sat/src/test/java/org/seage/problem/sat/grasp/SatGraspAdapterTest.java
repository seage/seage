package org.seage.problem.sat.grasp;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class SatGraspAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {

  public SatGraspAdapterTest() {
    super(new SatProblemProvider(), "GRASP");
  }
}
