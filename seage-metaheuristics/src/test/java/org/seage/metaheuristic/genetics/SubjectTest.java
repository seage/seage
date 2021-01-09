package org.seage.metaheuristic.genetics;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SubjectTest {
  private static Subject<Integer> s;

  @BeforeAll
  public static void setUpBeforeClass() throws Exception {
    s = new Subject<Integer>(new Integer[] { 1, 2, 3, 4 });
    s.setObjectiveValue(new double[] { 100 });
  }

  @Test
  void testClone() {
    assertNotNull(s);
    Subject<Integer> clone = s.clone();
    assertNotNull(clone);
    assertNotSame(s, clone);
    assertEquals(s.hashCode(), clone.hashCode());
    assertArrayEquals(s.getObjectiveValue(), clone.getObjectiveValue());

    for (int i = 0; i < s.getChromosome().getLength(); i++) {
      assertEquals(true, s.getChromosome().getGene(i).equals(clone.getChromosome().getGene(i)));
    }

    s.getChromosome().setGene(0, 5);
    s.computeHash();
    assertNotEquals(s.hashCode(), clone.hashCode());
  }

}
