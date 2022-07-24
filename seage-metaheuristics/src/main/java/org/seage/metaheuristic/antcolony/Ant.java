/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
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
 *
 * @author Martin Zaloga
 * @author Richard Malek (reworked 2021)
 */
public class Ant {

  protected Graph _graph;
  protected double _distanceTravelled;
  protected List<Edge> _edgePath;
  protected List<Node> _nodePath;

  protected double alpha;
  protected double beta;
  protected double quantumPheromone;

  protected HashSet<Node> availableNodes;
  private Random rand;

  public Ant( Graph graph, List<Integer> nodeIDs) {
     this(graph, nodeIDs, System.currentTimeMillis());
  }
  public Ant( Graph graph, List<Integer> nodeIDs, long randSeed) {
    _graph = graph;   
    _nodePath = new ArrayList<>();
    _edgePath = new ArrayList<>();
    rand = new Random(randSeed);

    if (nodeIDs != null) {
      for (int i = 0; i < nodeIDs.size() ; i++) {
        _nodePath.add(_graph.getNodes().get(nodeIDs.get(i)));
      }
    }   
  }

  void setParameters(double alpha, double beta, double quantumPheromone) {
    this.alpha = alpha;
    this.beta = beta;
    this.quantumPheromone = quantumPheromone;
  }

  /**
   * Do a first exploration if nodeIDs collection is set
   * 
   * @return A path traveled
   * @throws Exception
   */
  public List<Edge> doFirstExploration() throws Exception {
    _edgePath = new ArrayList<>();
    _distanceTravelled = 0;

    for (int i = 0; i < _nodePath.size() - 1; i++) {
      Node n1 = _nodePath.get(i);
      Node n2 = _nodePath.get(i + 1);
      Edge e = n1.getEdgeMap().get(n2);
      if (e == null){
        double edgePrice = getNodeDistance(_nodePath.subList(0, i+1), n2);
        e = new Edge(n1, n2, edgePrice);
      }
      _edgePath.add(e);
    }
    _distanceTravelled = getPathCost(_edgePath);
    leavePheromone();

    return _edgePath;
  }

  /**
   * Ant exploring the graph
   * 
   * @return Ant's path
   * @throws Exception
   */
  protected List<Edge> explore(Node startingNode) throws Exception {
    _nodePath.clear();
    _edgePath.clear();
    _distanceTravelled = 0;
    availableNodes = null;

    _nodePath.add(startingNode);

    Node currentNode = startingNode;
    Edge nextEdge = selectNextStep(_nodePath);

    while (nextEdge != null) {      
      Node nextNode = nextEdge.getNode2(currentNode);
   
      _edgePath.add(nextEdge);
      _nodePath.add(nextNode);
      currentNode = nextNode;

      nextEdge = selectNextStep(_nodePath);
    }
    _distanceTravelled = getPathCost(_edgePath);
    leavePheromone();
    return _edgePath;
  }

  /**
   * Pheromone leaving
   * @throws Exception
   */
  protected void leavePheromone() throws Exception {
    for (Edge edge : _edgePath) {
      edge.addLocalPheromone(quantumPheromone * (edge.getEdgePrice() / _distanceTravelled));
      if(!_graph._edges.contains(edge)) {
        _graph.addEdge(edge);
      }
    }
  }

  public List<Integer> getNodeIDsAlongPath() {
    List<Integer> idsPath = new ArrayList<>();
    for (Node n : _nodePath)
      idsPath.add(n.getID());
    return idsPath;
  }

  public double getDistanceTravelled() {
    return _distanceTravelled;
  }

  protected Edge selectNextStep(List<Node> nodePath) throws Exception {
    Node currentNode = nodePath.get(nodePath.size()-1);
    HashSet<Node> nextAvailableNodes = getAvailableNodes(nodePath);

    if (nextAvailableNodes.isEmpty())
      return null;

    int i = 0;
    double[] probabilities = new double[nextAvailableNodes.size()];
    Edge[] candidateEdges = new Edge[nextAvailableNodes.size()];
    
    // for each available node calculate probability
    for (Node n : nextAvailableNodes) {
      double edgePheromone = 0;
      double edgePrice = 0;

      Edge e = currentNode.getEdgeMap().get(n);
      if (e != null) {
        edgePheromone = e.getLocalPheromone();
        edgePrice = e.getEdgePrice();
      } else {
        edgePheromone = _graph.getDefaultPheromone();
        edgePrice = getNodeDistance(nodePath, n);
        e = new Edge(currentNode, n, edgePrice);
        e.setEdgePrice(edgePrice);
        e.addLocalPheromone(_graph.getDefaultPheromone());
      }

      double p = Math.pow(edgePheromone, alpha) * Math.pow(1 / edgePrice, beta);
      probabilities[i] = p;
      candidateEdges[i] = e;
      i++;
    }
    
    return candidateEdges[next(probabilities, rand.nextDouble())];
  }

  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    if(availableNodes == null) {
      availableNodes = new HashSet<Node>(_graph.getNodes().values());
    }
    Node lastNode = nodePath.get(nodePath.size()-1);
    availableNodes.remove(lastNode);   
    
    return availableNodes;
  }

   /**
   * Next edges index calculation.
   * @return - Next edges index
   */
  protected static int next(double[] probabilities, double randomNumber) {
    double[] probs = new double[probabilities.length];
    double sum = 0;
    for (int i = 0; i < probs.length; i++) {
      sum += probabilities[i]; 
      probs[i] = sum;
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

  public double getPathCost(List<Edge> path) throws Exception {
    double result = 0;
    for (Edge e : path) {
      result += e.getEdgePrice();
    }

    return result;
  }

  public double getNodeDistance(List<Node> nodePath, Node node) {
     return 1.0;
  };

}
