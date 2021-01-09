package org.seage.aal.algorithm.fireflies;

import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.Solution;

public class TestOperator extends FireflyOperator {

  @Override
  public double getDistance(Solution s1, Solution s2) {
    return 0;
  }

  @Override
  public void attract(Solution s0, Solution s1, int iter) {

  }

  @Override
  public Solution randomSolution() {
    return new TestSolution(new Object[] { 1, 3, 2, 5, 4 });
  }

  @Override
  public void randomSolution(Solution solution) {

  }

  @Override
  public void modifySolution(Solution solution) {

  }

}
