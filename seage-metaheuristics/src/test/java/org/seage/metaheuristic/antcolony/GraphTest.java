package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTest {
  Graph graph = new Graph(Arrays.asList(1, 2, 3));

  @BeforeEach
  public void init() throws Exception {    
    graph.setDefaultPheromone(1);
  }

  @Test
  public void testEdges() throws Exception {
    var nodes = graph.getNodes();
    
    Edge firstEdge = new Edge(nodes.get(1), nodes.get(2), 1);
    Edge secondEdge = new Edge(nodes.get(2), nodes.get(3), 1);

    // The first edge refers node1 and node2
    assertEquals(1, firstEdge.getNode1().getID());
    assertEquals(2, firstEdge.getNode2().getID());
    // The node1 and node2 are not wired with the edge
    assertEquals(0, nodes.get(1).getEdges().size());
    assertEquals(0, nodes.get(2).getEdges().size());

    graph.addEdge(firstEdge);
    graph.addEdge(secondEdge);
    
    // After adding edges to the graph the nodes are wired with the edges
    assertEquals(1, nodes.get(1).getEdges().size());    
    assertEquals(2, nodes.get(2).getEdges().size());
    assertEquals(1, nodes.get(3).getEdges().size());
    assertTrue(firstEdge.getNode1().getEdges().contains(firstEdge));

    // The graph has new edges
    assertTrue(graph.getEdges().contains(firstEdge));
    assertTrue(graph.getEdges().contains(secondEdge));
  }

  @Test
  public void testEdgeRemoval() throws Exception {
    var nodes = graph.getNodes();
    
    Edge firstEdge = new Edge(nodes.get(1), nodes.get(2), 1);
    Edge secondEdge = new Edge(nodes.get(2), nodes.get(3), 1);
    
    graph.addEdge(firstEdge);
    graph.addEdge(secondEdge);

    // The graph has new edges
    assertTrue(graph.getEdges().contains(firstEdge));
    assertTrue(graph.getEdges().contains(secondEdge));
    
    // Remove the first edge
    graph.removeEdge(firstEdge);
    // The graph does not contain the edge
    assertFalse(graph.getEdges().contains(firstEdge));
    // No node in the graph is wired with the edge
    assertFalse(nodes.get(1).getEdges().contains(firstEdge));
    assertFalse(nodes.get(2).getEdges().contains(firstEdge));
    assertFalse(nodes.get(3).getEdges().contains(firstEdge));

    // The first edge still refers nodes but they are not aware of the edge
    assertFalse(firstEdge.getNode1().getEdges().contains(firstEdge));
    assertFalse(firstEdge.getNode2().getEdges().contains(firstEdge));

    // The second edge is not affected by the removal
    assertTrue(graph.getEdges().contains(secondEdge));
    assertTrue(secondEdge.getNode1().getEdges().contains(secondEdge));
    assertTrue(secondEdge.getNode2().getEdges().contains(secondEdge));
  }
}
