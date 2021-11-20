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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Martin Zaloga
 */
public class Graph {

  protected HashMap<Integer, Node> _nodes;
  protected ArrayList<Edge> _edges;
  protected double _evaporCoeff = 0.95;
  private double _defaultPheromone;

  protected Graph() {
    _nodes = new HashMap<Integer, Node>();
    _edges = new ArrayList<Edge>();
  }

  protected Graph(List<Integer> nodeIDs) {
    _nodes = new HashMap<Integer, Node>();
    _edges = new ArrayList<Edge>();

    for(Integer id : nodeIDs) {
      _nodes.put(id, new Node(id));
    }
  }

  /**
   * List of nodes of graph
   * 
   * @return - List of nodes
   */
  public HashMap<Integer, Node> getNodes() {
    return _nodes;
  }

  /**
   * List of edges of graph
   * 
   * @return - List of edges
   */
  public ArrayList<Edge> getEdges() {
    return _edges;
  }

  /**
   * Evaporating from each edges of graph
   */
  public void evaporate() {
    for (Edge e : getEdges()) {
      e.evaporateFromEdge(_evaporCoeff);
    }
  }

  public double getDefaultPheromone() {
    return _defaultPheromone;
  }

  public void setDefaultPheromone(double defaultPheromone) {
    _defaultPheromone = defaultPheromone;
  }

  public void setEvaporCoeff(double evaporCoeff) {
    _evaporCoeff = evaporCoeff;
  }

  public void addEdge(Edge newEdge) throws Exception {
    if(!_nodes.containsValue(newEdge.getNode1())) {
      throw new Exception("Graph does not contain the node with id: " + newEdge.getNode1().getID());
    }
    if(!_nodes.containsValue(newEdge.getNode2())) {
      throw new Exception("Graph does not contain the node with id: " + newEdge.getNode2().getID());
    }

    Node n1 = newEdge.getNode1();
    n1.addEdge(newEdge);

    Node n2 = newEdge.getNode2();
    n2.addEdge(newEdge);    

    _edges.add(newEdge);
  }

  /**
   * This method removes the edge from both graph and nodes
   * @param edge
   * @throws Exception
   */
  public void removeEdge(Edge edge) throws Exception {
    // Remove the edge from edges
    this._edges.remove(edge);
    // Remove the edge from nodes
    Node n1 = edge.getNode1();    
    n1.removeEdge(edge);
    Node n2 = edge.getNode2();    
    n2.removeEdge(edge);
  }

  public void prune(long iteration) throws Exception {}

  public static List<Node> edgeListToNodeList(List<Edge> edges) {
    ArrayList<Node> nodeList = new ArrayList<Node>();

    Edge previous = null;
    for (Edge e : edges) {
      if (previous != null) {
        if (e.getNode1().getID() == previous.getNode1().getID() 
            || e.getNode1().getID() == previous.getNode2().getID()) {
          nodeList.add(e.getNode2());
        } else {
          nodeList.add(e.getNode1());
        }
      } else {
        nodeList.add(e.getNode1());
        nodeList.add(e.getNode2());
      }
      previous = e;
    }
    return nodeList;
  }

  public static List<Integer> edgeListToNodeIds(List<Edge> edges) {
    return Graph.edgeListToNodeList(edges)
        .stream()
        .map((Node n)->n.getID())
        .collect(Collectors.toList());
  }
}
