package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AntTest {
  Graph graph;

  @BeforeEach
  public void init() throws Exception {
    graph = new Graph(List.of(1, 2, 3));
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

  /**
   * Test tests the selection of the next step. Next edge
   * is generated by the selectNextStep method and this 
   * edge sould be the same as the one in the test.
   *
   * @throws Exception .
   */
  @Test
  void testGetAvailableNodes() {
    fail("TODO");
  }

  @Test
  public void testSelectNextStep() throws Exception {
    // Create new graph with four nodes
    Graph graph = new Graph(List.of(1, 2, 3, 4));

    var nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 2));
    graph.addEdge(new Edge(nodes.get(1), nodes.get(3), 4));
    graph.addEdge(new Edge(nodes.get(1), nodes.get(4), 2));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 4));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(4), 2));
    graph.addEdge(new Edge(nodes.get(3), nodes.get(4), 4));

    Random rand = new Random(42);

    Ant ant = new Ant(graph.nodesToNodePath(List.of(1, 2, 3)), 42);
    ant.setParameters(1, 1, 20);
    List<Edge> edgePath = ant.doFirstExploration(graph);
    List<Node> nodePath = Graph.edgeListToNodeList(edgePath);

    assertEquals(2, edgePath.size());
    assertEquals(3, nodePath.size());

    // Get the result of the calculate the Edges Heuristic (list of next moves)
    Ant.NextEdgeResult nextEdgeResult = ant.calculateEdgesHeuristic(graph, nodePath);

    double edgesHeuristicsSum = nextEdgeResult.getEdgesHeuristicsSum();
    List<Edge> edgeHeuristic = nextEdgeResult.getEdgesHeuristics();

    // Get the next edge by the selectNextStep method
    Edge nextEdge = ant.selectNextStep(graph, nodePath);

    // Simulate the the selection of next edge based on the random number (same seed as ant's)
    double randNum = rand.nextDouble();
    double tmpProb;
    double tmpSum = 0.0;

    for (int i = 0; i < edgeHeuristic.size(); i++) {
      tmpProb = (edgeHeuristic.get(i).getEdgeHeuristic() / edgesHeuristicsSum);
      tmpSum += tmpProb;
      if (tmpSum >= randNum) {
        // Test if given edge by selectNextStep is the same as by this simulated behavior
        assertEquals(edgeHeuristic.get(i).getNodes()[0].getID(), nextEdge.getNodes()[0].getID());
        assertEquals(edgeHeuristic.get(i).getNodes()[1].getID(), nextEdge.getNodes()[1].getID());
        break;
      }
    }
  }

  /**
   * Test tests calculation of the edges heuristic.
   *
   * @throws Exception .
   */
  @Test
  public void testCalculateEdgesHeuristic() throws Exception {
    // Create new graph with four nodes
    Graph gr = new Graph(List.of(1, 2, 3, 4));

    // Create graph's nodes and set their cost
    var nodes = gr.getNodes();
    gr.addEdge(new Edge(nodes.get(1), nodes.get(2), 2));
    gr.addEdge(new Edge(nodes.get(1), nodes.get(3), 4));
    gr.addEdge(new Edge(nodes.get(1), nodes.get(4), 2));
    gr.addEdge(new Edge(nodes.get(2), nodes.get(3), 4));
    gr.addEdge(new Edge(nodes.get(2), nodes.get(4), 2));
    gr.addEdge(new Edge(nodes.get(3), nodes.get(4), 4));

    // Create one ant and set variables
    Ant a = new Ant(gr.nodesToNodePath(List.of(1, 2, 3)), 42);
    a.setParameters(1, 1, 20);
    List<Edge> edges = a.doFirstExploration(gr);
    assertNotNull(edges);

    // Calculate the heuristic for next edges
    Ant.NextEdgeResult nextEdgeResult = a.calculateEdgesHeuristic(
        gr, Arrays.asList(edges.get(0).getNodes()));

    // Sum all the edges heuristic values
    double edgesHeuristicsSum = 0;
    for (Edge e : nextEdgeResult.getEdgesHeuristics()) {
      edgesHeuristicsSum += e.getEdgeHeuristic();
    }

    // Test if the summed value is same as the returned one
    assertEquals(nextEdgeResult.getEdgesHeuristicsSum(), edgesHeuristicsSum, 0.01);
  }

  @Test
  public void testLeavePheromone() throws Exception {
    graph.getEdges().clear();

    var nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 2));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 6));
    graph.addEdge(new Edge(nodes.get(1), nodes.get(3), 8));

    Ant a = new Ant(graph.nodesToNodePath(List.of(1, 2, 3)));
    a.setParameters(1, 1, 20);
    List<Edge> edges = a.doFirstExploration(graph);
    assertNotNull(edges);

    double firstEdgePheromone = edges.get(0).getLocalPheromone();
    double secondEdgePheromone = edges.get(1).getLocalPheromone();

    a.leavePheromone(graph, edges);

    assertEquals(8, a.getDistanceTravelled(graph, edges), 0.1);

    assertEquals(firstEdgePheromone 
        + (a.getQuantumPheromone() 
        * (edges.get(0).getEdgeCost() / a.getDistanceTravelled(graph, edges))), 
        edges.get(0).getLocalPheromone(), 0.1);
    assertEquals(secondEdgePheromone 
        + (a.getQuantumPheromone() 
        * (edges.get(1).getEdgeCost() / a.getDistanceTravelled(graph, edges))), 
        edges.get(1).getLocalPheromone(), 0.1);
  }
}

