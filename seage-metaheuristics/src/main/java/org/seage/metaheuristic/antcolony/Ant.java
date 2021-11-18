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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
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
import java.util.List;

/**
 *
 * @author Martin Zaloga
 */
public class Ant {

  protected Graph _graph;
  protected double _distanceTravelled;
  protected List<Edge> _edgePath;
  protected List<Node> _nodePath;

  protected AntBrain _brain;

  public Ant(AntBrain antBrain, Graph graph, List<Integer> nodeIDs) {
    _brain = antBrain;
    _graph = graph;   
    _nodePath = new ArrayList<>();
    _edgePath = new ArrayList<>();

    if (nodeIDs != null)
      for (int i = 0; i < nodeIDs.size() ; i++)
        _nodePath.add(_graph.getNodes().get(nodeIDs.get(i)));
    
  }

  void setParameters(double alpha, double beta, double quantumPheromone) {

    _brain.setParameters(alpha, beta, quantumPheromone);
  }

  /**
   * Do a first exploration if nodeIDs collection is set
   * 
   * @return A path traveled
   * @throws Exception
   */
  public List<Edge> doFirstExploration() throws Exception {
    _edgePath = new ArrayList<Edge>();
    _distanceTravelled = 0;

    for (int i = 0; i < _nodePath.size() - 1; i++) {
      Node n1 = _nodePath.get(i);
      Node n2 = _nodePath.get(i + 1);
      Edge e = n1.getEdgeMap().get(n2);
      if (e == null){
        e = _brain.createEdge(n1, n2);
        _graph.addEdge(e);
      }
      _edgePath.add(e);
    }
    _distanceTravelled = _brain.getPathCost(_edgePath);
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
    _brain.reset();

    _nodePath.add(startingNode);

    Node currentNode = startingNode;
    Node nextNode = _brain.selectNextStep(_nodePath).getNode2();

    while (nextNode != null) {
      Edge nextEdge = currentNode.getEdgeMap().get(nextNode);
      if (nextEdge == null) {
        nextEdge = nextNode.getEdgeMap().get(currentNode);
        if (nextEdge == null) {
          nextEdge = _brain.createEdge(currentNode, nextNode);
          _graph.addEdge(nextEdge);
        }
        else
          nextNode.getEdgeMap().put(currentNode, nextEdge);
      }

      _edgePath.add(nextEdge);
      _nodePath.add(nextNode);

      currentNode = nextNode;
      nextNode = _brain.selectNextStep(_nodePath).getNode2();
    }
    _distanceTravelled = _brain.getPathCost(_edgePath);
    leavePheromone();
    return _edgePath;
  }

  /**
   * Pheromone leaving
   */
  protected void leavePheromone() {
    for (Edge edge : _edgePath) {
      edge.addLocalPheromone(_brain.getQuantumPheromone() / (_distanceTravelled));
    }
  }

  public AntBrain getBrain() {
    return _brain;
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
}
