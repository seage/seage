package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
   * Testing the return of the right nodes (not yet visited).
   *
   * @throws Exception .
   */
  @Test
  void testGetAvailableNodes() throws Exception {
    // Create new graph with four nodes
    Graph graph = new Graph(List.of(1, 2, 3, 4));
    // Create new ant ant set its values
    Ant ant = new Ant(graph.nodesToNodePath(List.of(1, 2)), 42);
    ant.setParameters(1, 1, 20);
    // Get the list of edges on the path
    List<Edge> edgePath = ant.doFirstExploration(graph);
    // Get list of nodes
    List<Node> nodePath = Graph.edgeListToNodeList(edgePath);

    // Get available nodes (all - those already visited)
    HashSet<Node> availNodes = ant.getAvailableNodes(graph, nodePath);

    // Test if the avaiable nodes are those not yet visited
    assertFalse(availNodes.contains(graph.getNodes().get(1)));
    assertFalse(availNodes.contains(graph.getNodes().get(2)));
    assertTrue(availNodes.contains(graph.getNodes().get(3)));
    assertTrue(availNodes.contains(graph.getNodes().get(4)));
  }

  @Test
  public void testSelectNextStep() throws Exception {
    // Create new graph with four nodes
    Graph graph = new Graph(List.of(1, 2, 3, 4));

    HashMap<Integer, Node> nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 1));
    graph.addEdge(new Edge(nodes.get(1), nodes.get(3), 2));
    graph.addEdge(new Edge(nodes.get(1), nodes.get(4), 3));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 4));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(4), 5));
    graph.addEdge(new Edge(nodes.get(3), nodes.get(4), 6));

    Ant ant = new Ant(graph.nodesToNodePath(List.of(1, 2)), 42);
    ant.setParameters(1, 1, 20);
    List<Edge> edgePath = ant.doFirstExploration(graph);
    List<Node> nodePath = Graph.edgeListToNodeList(edgePath);
    // Get the next edge by the selectNextStep method
    Edge nextEdge = ant.selectNextStep(graph, nodePath);

    assertEquals(2, nextEdge.getNodes()[0].getID());
    assertEquals(4, nextEdge.getNodes()[1].getID());
    assertEquals(5, nextEdge.getEdgeCost());
    assertEquals(0.0002, nextEdge.getEdgeHeuristic(), 1E-5);
  }

  @Test
  void testCalculateEdgesHeuristicp() throws Exception {
    // Create new graph with four nodes
    Graph graph = new Graph(List.of(1, 2, 3, 4));

    HashMap<Integer, Node> nodes = graph.getNodes();
    graph.addEdge(new Edge(nodes.get(1), nodes.get(2), 1));
    graph.addEdge(new Edge(nodes.get(1), nodes.get(3), 2));
    graph.addEdge(new Edge(nodes.get(1), nodes.get(4), 3));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(3), 4));
    graph.addEdge(new Edge(nodes.get(2), nodes.get(4), 5));
    graph.addEdge(new Edge(nodes.get(3), nodes.get(4), 6));

    // Create and set new ant
    List<Node> nodePath = graph.nodesToNodePath(List.of(1));
    Ant ant = new Ant(nodePath, 42);
    ant.setParameters(1, 1, 20);

    // Get the result of the calculate the Edges Heuristic (list of next moves)
    Ant.NextEdgeResult nextEdgeResult = ant.calculateEdgesHeuristic(graph, nodePath);
    List<Edge> nextEdges = nextEdgeResult.getEdgesHeuristics();
    assertEquals(3, nextEdges.size());
    assertEquals(0.001, nextEdges.get(0).getEdgeHeuristic());
    assertEquals(0.0005, nextEdges.get(1).getEdgeHeuristic());
    assertEquals(0.000333, nextEdges.get(2).getEdgeHeuristic(), 1E-6);

    // Test the sum is correct
    double edgesHeuristicsSum1 = nextEdgeResult.getEdgesHeuristicsSum();
    double edgesHeuristicsSum2 =
        nextEdges.stream().map(e -> e.getEdgeHeuristic()).reduce(0d, Double::sum);

    assertEquals(edgesHeuristicsSum1, edgesHeuristicsSum2);
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
    Ant.NextEdgeResult nextEdgeResult =
        a.calculateEdgesHeuristic(gr, Arrays.asList(edges.get(0).getNodes()));

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

    // TODO: ???
    assertEquals(
        firstEdgePheromone + (a.getQuantumPheromone()
            * (edges.get(0).getEdgeCost() / a.getDistanceTravelled(graph, edges))),
        edges.get(0).getLocalPheromone(), 0.1);
    assertEquals(
        secondEdgePheromone + (a.getQuantumPheromone()
            * (edges.get(1).getEdgeCost() / a.getDistanceTravelled(graph, edges))),
        edges.get(1).getLocalPheromone(), 0.1);
  }
}

