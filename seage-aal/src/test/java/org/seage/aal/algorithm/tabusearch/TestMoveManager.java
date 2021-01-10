package org.seage.aal.algorithm.tabusearch;

import java.util.Random;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;

public class TestMoveManager implements MoveManager {
  private Random rnd = new Random(1);

  @Override
  public Move[] getAllMoves(Solution solution) throws Exception {
    Move m = new Move() {
      @Override
      public void operateOn(Solution soln) {
        TestSolution s = (TestSolution) soln;
        int ix = rnd.nextInt(s.solution.length);
        Integer o = s.solution[0];
        s.solution[0] = s.solution[ix];
        s.solution[ix] = o;
      }
    };
    return new Move[] { m, m };
  }

}
