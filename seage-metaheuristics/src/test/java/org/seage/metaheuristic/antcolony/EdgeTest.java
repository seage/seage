package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

class EdgeTest {
  
  @Test
  void testHashCode() {
    Edge e1 = new Edge(new Node(1), new Node(2), 0);
    Edge e2 = new Edge(new Node(2), new Node(1), 0);

    assertEquals(e1.hashCode(), e2.hashCode());
  }

  @Test
  void testEquals() {
    // TODO
  }

  @Test
  void testEdgeInHashSet() {
    Edge e1 = new Edge(new Node(1), new Node(2), 0);
    Edge e2 = new Edge(new Node(2), new Node(1), 0);

    HashSet<Edge> set = new HashSet<>();

    set.add(e1);
    set.add(e2);
    
    assertEquals(1, set.size());
  }
}
