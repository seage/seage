package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    assertTrue(set.contains(e1));
    assertTrue(set.contains(e2));
  }

  @Test
  void testEvaporateFromEdge() {
    Edge e1 = new Edge(new Node(1), new Node(2), 0);
    e1.addLocalPheromone(1);

    e1.evaporateFromEdge(0.5);

    assertEquals(0.5, e1.getLocalPheromone());

    // In the corner case the evaporate from edge
    // shouldn't let the pheromone go under 0.00001
    e1.evaporateFromEdge(0.99999);

    assertEquals(0.00001, e1.getLocalPheromone());
  }
}
