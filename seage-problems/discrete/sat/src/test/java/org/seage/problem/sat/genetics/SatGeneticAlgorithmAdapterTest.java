package org.seage.problem.sat.genetics;

import org.seage.aal.problem.ProblemAlgorithmAdapterTestBase;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatProblemProvider;

public class SatGeneticAlgorithmAdapterTest extends ProblemAlgorithmAdapterTestBase<SatPhenotype> {
  public SatGeneticAlgorithmAdapterTest() {
    super(new SatProblemProvider(), "GeneticAlgorithm");
  }
}
