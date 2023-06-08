package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AntTest {
  Graph graph;

  @BeforeEach
  public void init() throws Exception {
    graph = new Graph(List.of(1, 2, 3));
    graph.setDefaultPheromone(1);    
  }

  @Test
  public void testAntFirstExplorationNoInitPath() throws Exception {
    var nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 1));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 1));

    Ant a = new Ant();
    a.setParameters(1, 1, 20);
    List<Edge> edges = a.doFirstExploration(graph);
    assertNotNull(edges);
    assertEquals(0, edges.size());    
    assertEquals(2, graph.getEdges().size());
  }

  @Test
  public void testAntFirstExplorationWithInitPath() throws Exception {
    var nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 1));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 1));

    Ant a = new Ant(graph.nodesToNodePath(List.of(1, 2, 3)));
    a.setParameters(1, 1, 20);
    List<Edge> edges = a.doFirstExploration(graph);
    assertNotNull(edges);
    assertEquals(2, edges.size());
    assertEquals(2, graph.getEdges().size());
    assertTrue(graph.getEdges().containsAll(edges));
    ArrayList<Edge> edgesList = new ArrayList<>(graph.getEdges());
    assertEquals(10, edgesList.get(0).getLocalPheromone());
    assertEquals(10, edgesList.get(1).getLocalPheromone());
  }

  @Test
  public void testAntFirstExplorationWithInitPathNoEdges() throws Exception {
    graph.getEdges().clear();
    Ant a = new Ant(graph.nodesToNodePath(List.of(1, 2, 3)));
    a.setParameters(1, 1, 20);
    List<Edge> edges = a.doFirstExploration(graph);
    assertNotNull(edges);
    assertEquals(2, edges.size());
    assertEquals(2, graph.getEdges().size());
    assertTrue(graph.getEdges().containsAll(edges));
    ArrayList<Edge> edgesList = new ArrayList<>(graph.getEdges());
    assertEquals(10, edgesList.get(0).getLocalPheromone());
    assertEquals(10, edgesList.get(1).getLocalPheromone());
  }

  @Test
  public void testSelectNextStep() throws Exception{
    graph.getEdges().clear();

    Ant a = new Ant(graph.nodesToNodePath(List.of(1, 2, 3)));
    a.setParameters(1, 1, 20);
    List<Edge> edges = a.doFirstExploration(graph);
    assertNotNull(edges);

    Edge edg = a.selectNextStep(graph, Arrays.asList(edges.get(0).getNodes()));

    assertEquals(2, edg.getNodes()[0].getID());
    assertEquals(3, edg.getNodes()[1].getID());
  }
}

