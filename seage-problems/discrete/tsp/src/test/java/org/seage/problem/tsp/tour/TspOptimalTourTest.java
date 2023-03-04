package org.seage.problem.tsp.tour;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class TspOptimalTourTest {
  @Test
  void testBerlin52() {
    assertEquals("berlin52", TspOptimalTourBerlin52.Name);
  }
}
