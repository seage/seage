package org.seage.aal.algorithm.fireflies;

import org.seage.metaheuristic.fireflies.Solution;
import org.seage.metaheuristic.fireflies.SolutionAdapter;

public class TestSolution extends SolutionAdapter {
  public Object[] solution;

  public TestSolution(Object[] sol) {
    solution = sol;
  }

  @Override
  public boolean equals(Solution s) {
    return false;
  }

}
