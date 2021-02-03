package org.seage.problem.tsp.antcolony;

import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

// @Disabled("Adapter class not fully implemented yet")
public class TspAntColonyAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {

  public TspAntColonyAdapterTest() {
    super(new TspProblemProvider(), "AntColony");
  }

}
