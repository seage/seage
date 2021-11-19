package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTest {
  Graph graph = new Graph(Arrays.asList(1, 2, 3));

  @BeforeEach
  public void init() throws Exception {    
    graph.setDefaultPheromone(1);
  }

  @Test
  public void testEdgeRemoval() throws Exception {
    var nodes = graph.getNodes();
    
    Edge firstEdge = new Edge(nodes.get(1), nodes.get(2), 1);
    Edge secondEdge = new Edge(nodes.get(2), nodes.get(3), 1);

    assertEquals(1, firstEdge.getNode1().getID());
    assertEquals(2, firstEdge.getNode2().getID());

    assertEquals(0, nodes.get(1).getEdges().size());
    assertEquals(0, nodes.get(2).getEdges().size());

    graph.addEdge(firstEdge);
    graph.addEdge(secondEdge);

    assertEquals(1, nodes.get(1).getEdges().size());
    assertEquals(2, nodes.get(2).getEdges().size());
    assertEquals(1, nodes.get(3).getEdges().size());

    assertTrue(graph.getEdges().contains(firstEdge));
    assertTrue(firstEdge.getNode1().getEdges().contains(firstEdge));

    // Remove the first edge
    graph.removeEdge(firstEdge);

    assertFalse(graph.getEdges().contains(firstEdge));
    assertFalse(firstEdge.getNode1().getEdges().contains(firstEdge));

    assertTrue(graph.getEdges().contains(secondEdge));
    assertTrue(secondEdge.getNode1().getEdges().contains(secondEdge));
  }
}
