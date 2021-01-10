package org.seage.problem.sat.tabusearch;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

public class SatTabuSearchAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {

  public SatTabuSearchAdapterTest() {
    super(new SatProblemProvider(), "TabuSearch");
  }
}
