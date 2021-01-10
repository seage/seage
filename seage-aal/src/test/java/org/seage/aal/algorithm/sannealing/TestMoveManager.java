package org.seage.aal.algorithm.sannealing;

import java.util.Random;

import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

public class TestMoveManager implements IMoveManager {
  private Random rnd = new Random(1);

  @Override
  public Solution getModifiedSolution(Solution solution, double ct) {
    TestSolution s = (TestSolution) solution.clone();
    int ix = rnd.nextInt(s.solution.length);
    Integer o = s.solution[0];
    s.solution[0] = s.solution[ix];
    s.solution[ix] = o;
    return s;
  }

}
