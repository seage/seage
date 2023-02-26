package org.seage.problem.jsp.tabusearch;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

public class JspMoveTest {

  @Test
  void testMove() {
    JspMove move = new JspMove(1, 2);
    JspTabuSearchSolution solution = new JspTabuSearchSolution(new Integer[] {1, 2, 3, 4, 5});

    assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, solution.getJobArray());
    move.operateOn(solution);
    assertArrayEquals(new Integer[] {1, 3, 2, 4, 5}, solution.getJobArray());
    move.operateOn(solution);
    assertArrayEquals(new Integer[] {1, 2, 3, 4, 5}, solution.getJobArray());
  }
}