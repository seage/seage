package org.seage.problem.tsp.antcolony;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemProvider;

public class TspAntColonyAdapterTest extends ProblemAlgorithmAdapterTestBase<TspPhenotype> {

  public TspAntColonyAdapterTest() {
    super(new TspProblemProvider(), "AntColony");
  }

}
