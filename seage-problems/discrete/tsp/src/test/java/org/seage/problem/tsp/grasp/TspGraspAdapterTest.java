package org.seage.problem.tsp.grasp;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

@Disabled("Adapter class not fully implemented yet")
public class TspGraspAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {
  public TspGraspAdapterTest() {
    super(new TspProblemProvider(), "GRASP");
  }

}
