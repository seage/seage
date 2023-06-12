/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * .
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     Martin Zaloga
 *     - Reimplementation
 */

package org.seage.metaheuristic.antcolony;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * .
 *
 * @author Martin Zaloga
 * @author Richard Malek (reworked 2021)
 */
public class Ant {

  protected List<Node> nodePath;

  protected double alpha;
  protected double beta;
  protected double quantumPheromone;

  private Random rand;

  public Ant() {
    this(null, System.currentTimeMillis());
  }

  public Ant(List<Node> initNodePath) {
    this(initNodePath, System.currentTimeMillis());
  }

  /**
   * .
   *
   * @param initNodePath . 
   * @param randSeed .
   */
  public Ant(List<Node> initNodePath, long randSeed) {    
    this.nodePath = new ArrayList<>();
    this.rand = new Random(randSeed);
    
    if (initNodePath != null) {
      nodePath.addAll(initNodePath);
    }
  }

  /**
   * Class represents the result of searching for candidate
   * edges. Contains the sum of counted edges heuristics and 
   * the list of candidate edges (each with counted edge heuristic).
   */
  protected class NextEdgeResult {
    private double edgeHeuristicsSum;
    private List<Edge> edgeHeuristics;

    public NextEdgeResult(double edgeHeurSum, List<Edge> edgeHeuristics) {
      this.edgeHeuristicsSum = edgeHeurSum;
      this.edgeHeuristics = edgeHeuristics;
    }

    public double getEdgesHeuristicsSum() {
      return this.edgeHeuristicsSum;
    }

    public List<Edge> getEdgesHeuristics() {
      return this.edgeHeuristics;
    }
  }

  void setParameters(double alpha, double beta, double quantumPheromone) {
    this.alpha = alpha;
    this.beta = beta;
    this.quantumPheromone = quantumPheromone;
  }

  /**
   * Do a first exploration if nodeIDs collection is set.
   *
   * @return A path traveled
   * @throws Exception .
   */
  public List<Edge> doFirstExploration(Graph graph) throws Exception {
    List<Edge> edgePath = new ArrayList<>();
    
    for (int i = 0; i < nodePath.size() - 1; i++) {
      Node n1 = nodePath.get(i);
      Node n2 = nodePath.get(i + 1);
      Edge e = n1.getEdge(n2);
      if (e == null) {
        double edgeCost = getNodeDistance(graph, nodePath.subList(0, i + 1), n2);
        e = new Edge(n1, n2, edgeCost);
      }
      edgePath.add(e);
    }
    leavePheromone(graph, edgePath);

    return edgePath;
  }

  /**
   * Ant exploring the graph.
   *
   * @return Ant's path
   * @throws Exception .
   */
  protected List<Edge> explore(Graph graph, Node startingNode) throws Exception {
    nodePath.clear();
    
    List<Edge> edgePath = new ArrayList<>();

    nodePath.add(startingNode);

    Node currentNode = startingNode;
    Edge nextEdge = selectNextStep(graph, nodePath);

    while (nextEdge != null) {      
      Node nextNode = nextEdge.getNode2(currentNode);
   
      edgePath.add(nextEdge);
      nodePath.add(nextNode);
      currentNode = nextNode;

      nextEdge = selectNextStep(graph, nodePath);
    }
    leavePheromone(graph, edgePath);
    return edgePath;
  }

  /**
   * Pheromone leaving.
   *
   * @throws Exception .
   */
  protected void leavePheromone(Graph graph, List<Edge> edgePath) throws Exception {
    double distanceTravelled = getDistanceTravelled(graph, edgePath);
    for (Edge edge : edgePath) {  
      double res = quantumPheromone * (
          edge.getEdgeCost() / distanceTravelled);
      edge.addLocalPheromone(res);
      if (!graph._edges.contains(edge)) { // TODO: Refactor, ineffective
        graph.addEdge(edge);
      }
    }
  }

  /**
   * .
   *
   * @return Node ids along the path.
   */
  public List<Integer> getNodeIDsAlongPath() {
    List<Integer> idsPath = new ArrayList<>();
    for (Node n : nodePath) {
      idsPath.add(n.getID());
    }
    return idsPath;
  }

  /**
   * Method calculates cost of travelled path.
   *
   * @param graph Graph.
   * @param edgePath Edges along the path.
   * @return Returns travelled distance.
   * @throws Exception .
   */
  public double getDistanceTravelled(Graph graph, List<Edge> edgePath) throws Exception {

    double result = 0;
    for (Edge e : edgePath) {
      result += e.getEdgeCost();
    }

    return result;
  }

  protected Edge selectNextStep(Graph graph, List<Node> nodePath) throws Exception {
    // Calculate the edges heuristics and its sum
    NextEdgeResult nextEdgeResult = calculateEdgesHeuristic(graph, nodePath);

    // No candidate edges available
    if (nextEdgeResult == null) {
      return null;
    }

    double edgesHeuristicsSum = nextEdgeResult.getEdgesHeuristicsSum();
    List<Edge> candidateEdges = nextEdgeResult.getEdgesHeuristics();

    // Throw exception if sum of all edges' prices is zero
    if (edgesHeuristicsSum == 0.0) {
      throw new ArithmeticException();
    }

    // Get next edge
    double randNum = rand.nextDouble();
    double tmpProb;
    double tmpSum = 0.0;
    for (int i = 0; i < candidateEdges.size(); i++) {
      tmpProb = (candidateEdges.get(i).getEdgeHeuristic() / edgesHeuristicsSum);
      tmpSum += tmpProb;
      if (tmpSum >= randNum) {
        return candidateEdges.get(i);
      }
    }
    return null;
  }

  protected NextEdgeResult calculateEdgesHeuristic(
      Graph graph, List<Node> nodePath) {
    
    Node currentNode = nodePath.get(nodePath.size() - 1);
    HashSet<Node> nextAvailableNodes = getAvailableNodes(graph, nodePath);

    if (nextAvailableNodes.isEmpty()) {
      return null;
    }

    List<Edge> candidateEdges = new ArrayList<>();
    double sumCostEdges = 0.0;
    
    // Find all candidate edges
    for (Node n : nextAvailableNodes) {
      double nodeDistance = 0;

      Edge e = currentNode.getEdge(n);
      if (e == null) {
        nodeDistance = getNodeDistance(graph, nodePath, n);

        e = new Edge(currentNode, n, nodeDistance);
      }
      
      double edgeHeuristic = Math.pow(
          e.getLocalPheromone(), alpha) * Math.pow(1 / e.getEdgeCost(), beta);
      if (edgeHeuristic == 0) {
        edgeHeuristic = Math.pow(1 / e.getEdgeCost(), beta);
      }

      e.setEdgeHeuristic(edgeHeuristic);
      sumCostEdges += edgeHeuristic;

      candidateEdges.add(e);
    }

    return new NextEdgeResult(sumCostEdges, candidateEdges);
  }

  protected HashSet<Node> getAvailableNodes(Graph graph, List<Node> nodePath) {
    HashSet<Node> availableNodes = new HashSet<>(graph.getNodes().values());
    availableNodes.removeAll(nodePath);   
    
    return availableNodes;
  }

  public double getQuantumPheromone() {
    return quantumPheromone;
  }

  public double getNodeDistance(Graph graph, List<Node> nodePath, Node node) {
    return 1.0;
  }
}
