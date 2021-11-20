package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class AntBrainTest {
  @Test
  public void testNextEdge() throws Exception {
    double[] probs = new double[]{0.3, 0.1, 0.4, 0.2};
    assertEquals(0, AntBrain.next(probs, 0.2));
    assertEquals(0, AntBrain.next(probs, 0.3));
    assertEquals(1, AntBrain.next(probs, 0.301));
    assertEquals(1, AntBrain.next(probs, 0.4));
    assertEquals(2, AntBrain.next(probs, 0.5));
    assertEquals(2, AntBrain.next(probs, 0.8));
    assertEquals(3, AntBrain.next(probs, 0.801));
    assertEquals(3, AntBrain.next(probs, 1.0));
  }
}
