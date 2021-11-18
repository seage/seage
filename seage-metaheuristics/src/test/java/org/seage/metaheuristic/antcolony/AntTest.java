package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AntTest {
  Graph graph = new Graph(Arrays.asList(1, 2, 3));
  AntBrain antBrain = new AntBrain(graph);

  @BeforeEach
  public void init() throws Exception {    
    graph.setDefaultPheromone(1);    
    antBrain.setParameters(1, 1, 20);
  }

  @Test
  public void testAntFirstExplorationNoInitPath() throws Exception {
    var nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 1));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 1));

    Ant a = new Ant(antBrain, graph, null);
    List<Edge> edges = a.doFirstExploration();
    assertNotNull(edges);
    assertEquals(0, edges.size());    
    assertEquals(2, graph.getEdges().size());
  }

  @Test
  public void testAntFirstExplorationWithInitPath() throws Exception {
    var nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 1));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 1));

    Ant a = new Ant(antBrain, graph, Arrays.asList(1, 2, 3));
    List<Edge> edges = a.doFirstExploration();
    assertNotNull(edges);
    assertEquals(2, edges.size());
    assertEquals(2, graph.getEdges().size());
    assertTrue(graph.getEdges().containsAll(edges));
    assertEquals(10, graph.getEdges().get(0).getLocalPheromone());
    assertEquals(10, graph.getEdges().get(1).getLocalPheromone());
  }

  @Test
  public void testAntFirstExplorationWithInitPathNoEdges() throws Exception {
    graph.getEdges().clear();
    Ant a = new Ant(antBrain, graph, Arrays.asList(1, 2, 3));
    List<Edge> edges = a.doFirstExploration();
    assertNotNull(edges);
    assertEquals(2, edges.size());
    assertEquals(2, graph.getEdges().size());
    assertTrue(graph.getEdges().containsAll(edges));
    assertEquals(10, graph.getEdges().get(0).getLocalPheromone());
    assertEquals(10, graph.getEdges().get(1).getLocalPheromone());
  }
}

