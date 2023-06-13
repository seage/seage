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
   * This test tests if the correct set of nodes is 
   * returnes (the ones that haven't been visited).
   *
   * @throws Exception .
   */
  @Test
  void testGetAvailableNodes() throws Exception {
    Graph graph = new Graph(List.of(1, 2, 3, 4));
    // The ant initially travels through two nodes, thus next two available nodes expected.
    Ant ant = new Ant(graph.nodesToNodePath(List.of(1, 2)), 42);
    ant.setParameters(1, 1, 20);

    List<Edge> edgePath = ant.doFirstExploration(graph);
    List<Node> nodePath = Graph.edgeListToNodeList(edgePath);

    // It should return not yet visited nodes (the rest two)
    HashSet<Node> availNodes = ant.getAvailableNodes(graph, nodePath);

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
    // Create new graph with four nodes
    Graph gr = new Graph(List.of(1, 2, 3, 4));
    var nodes = gr.getNodes();
    gr.addEdge(new Edge(nodes.get(1), nodes.get(2), 2));
    gr.addEdge(new Edge(nodes.get(1), nodes.get(3), 4));
    gr.addEdge(new Edge(nodes.get(1), nodes.get(4), 2));
    gr.addEdge(new Edge(nodes.get(2), nodes.get(3), 4));
    gr.addEdge(new Edge(nodes.get(2), nodes.get(4), 2));
    gr.addEdge(new Edge(nodes.get(3), nodes.get(4), 2));

    Ant ant1 = new Ant(gr.nodesToNodePath(List.of(1, 2, 4, 3, 1)));
    ant1.setParameters(1, 1, 20);
    List<Edge> edges1 = ant1.doFirstExploration(gr);
    assertNotNull(edges1);

    ant1.leavePheromone(gr, edges1);

    // Ant should use the shortest path
    assertEquals(10, ant1.getDistanceTravelled(gr, edges1), 0.1);

    // Following the pheromone update function these values has to be the same
    assertEquals(4.0, edges1.get(0).getLocalPheromone());
    assertEquals(4.0, edges1.get(1).getLocalPheromone());
    assertEquals(4.0, edges1.get(2).getLocalPheromone());

    // The same gr with the same (unchanged) edges with different path
    // should result in worse (lower) edges pheromones
    gr = new Graph(List.of(1, 2, 3, 4));
    nodes = gr.getNodes();
    gr.addEdge(new Edge(nodes.get(1), nodes.get(2), 2));
    gr.addEdge(new Edge(nodes.get(1), nodes.get(3), 4));
    gr.addEdge(new Edge(nodes.get(1), nodes.get(4), 2));
    gr.addEdge(new Edge(nodes.get(2), nodes.get(3), 4));
    gr.addEdge(new Edge(nodes.get(2), nodes.get(4), 2));
    gr.addEdge(new Edge(nodes.get(3), nodes.get(4), 2));

    // Second ant will use different path (different total cost)
    Ant ant2 = new Ant(gr.nodesToNodePath(List.of(1, 3, 2, 4, 1)));
    ant2.setParameters(1, 1, 20);
    List<Edge> edges2 = ant2.doFirstExploration(gr);
    assertNotNull(edges2);
    
    ant2.leavePheromone(gr, edges2);

    // Different path results in worse distance traveled and worse pheromone placed
    assertEquals(12, ant2.getDistanceTravelled(gr, edges2), 0.1);
    assertEquals(3.33, edges2.get(0).getLocalPheromone(), 0.01);
    assertEquals(3.33, edges2.get(1).getLocalPheromone(), 0.01);
    assertEquals(3.33, edges2.get(2).getLocalPheromone(), 0.01);
  }
}

