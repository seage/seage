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
  protected List<Integer> initialPath;

  protected double alpha;
  protected double beta;
  protected double quantumPheromone;

  private Random rand;

  public Ant() {
    this(null, System.currentTimeMillis());
  }

  public Ant(List<Integer> initialPath) {
    this(initialPath, System.currentTimeMillis());
  }

  /**
   * .
   *
   * @param initialPath . 
   * @param randSeed .
   */
  public Ant(List<Integer> initialPath, long randSeed) {
    this.initialPath = initialPath;
    this.nodePath = new ArrayList<>();
    this.rand = new Random(randSeed);
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
    this.nodePath = new ArrayList<>();

    List<Edge> edgePath = new ArrayList<>();
    if (initialPath != null) {
     
      for (int i = 0; i < initialPath.size(); i++) {
        this.nodePath.add(graph.getNodes().get(initialPath.get(i)));
      }
    }   
    
    for (int i = 0; i < nodePath.size() - 1; i++) {
      Node n1 = nodePath.get(i);
      Node n2 = nodePath.get(i + 1);
      Edge e = n1.getEdgeMap().get(n2);
      if (e == null) {
        double edgePrice = getNodeDistance(graph, nodePath.subList(0, i + 1), n2);
        e = new Edge(n1, n2, edgePrice);
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
   * @throws Exception .
   */
  protected void leavePheromone(Graph graph, List<Edge> edgePath) throws Exception {
    double distanceTravelled = getDistanceTravelled(graph, edgePath);
    for (Edge edge : edgePath) {      
      edge.addLocalPheromone(quantumPheromone * (
          edge.getEdgePrice() / distanceTravelled));
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
      result += e.getEdgePrice();
    }

    return result;
  }

  protected Edge selectNextStep(Graph graph, List<Node> nodePath) throws Exception {
    Node currentNode = nodePath.get(nodePath.size() - 1);
    HashSet<Node> nextAvailableNodes = getAvailableNodes(graph, nodePath);

    if (nextAvailableNodes.isEmpty()) {
      return null;
    }

    List<Double> edgeCosts = new ArrayList<>();
    List<Edge> candidateEdges = new ArrayList<>();
    double sumCostEdges = 0.0;
    
    // Find all candidate edges
    for (Node n : nextAvailableNodes) {
      double edgePheromone = 0;
      double edgePrice = 0;

      Edge e = currentNode.getEdgeMap().get(n);
      if (e == null) {
        edgePheromone = graph.getDefaultPheromone();
        edgePrice = getNodeDistance(graph, nodePath, n);

        e = new Edge(currentNode, n, edgePrice);
        e.addLocalPheromone(edgePheromone);
      }
      candidateEdges.add(e);
      double edgeCost = Math.pow(
          e.getLocalPheromone(), alpha) * Math.pow(1 / e.getEdgePrice(), beta);
      if (edgeCost == 0) {
        edgeCost = Math.pow(1 / e.getEdgePrice(), beta);
      }
      edgeCosts.add(edgeCost);
      sumCostEdges += edgeCost;
    }

    // Throw exception if sum of all edges' prices is zero
    if (sumCostEdges == 0.0) {
      throw new ArithmeticException();
    }

    // Get next edge
    double randNum = rand.nextDouble();
    double tmpProb;
    double tmpSum = 0.0;
    for (int i = 0; i < edgeCosts.size(); i++) {
      tmpProb = (edgeCosts.get(i) / sumCostEdges);
      tmpSum += tmpProb;
      if (tmpSum >= randNum) {
        return candidateEdges.get(i);
      }
    }
    return null;
  }

  protected HashSet<Node> getAvailableNodes(Graph graph, List<Node> nodePath) {
    HashSet<Node> availableNodes = new HashSet<>(graph.getNodes().values());
    availableNodes.removeAll(nodePath);   
    
    return availableNodes;
  }

  /**
   * Next edges index calculation.
   *
   * @return - Next edges index
   * @throws Exception .
   */
  protected static int next(double[] probabilities, double randomNumber) throws Exception {
    double[] probs = new double[probabilities.length];
    double sum = 0;
    for (int i = 0; i < probs.length; i++) {
      sum += probabilities[i]; 
      probs[i] = sum;
    }
    if (sum == 0) {
      throw new Exception("Unexpected value of sum: 0");
    }
    for (int i = 0; i < probs.length; i++) {
      probs[i] /= sum;
    }
    for (int i = 0; i < probs.length; i++) {
      if (randomNumber <= probs[i]) {
        return i;
      }
    }
    return 0;
  }

  public double getQuantumPheromone() {
    return quantumPheromone;
  }

  public double getNodeDistance(Graph graph, List<Node> nodePath, Node node) {
    return 1.0;
  }

}
