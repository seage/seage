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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * .
 * @author Martin Zaloga
 */
public class Graph {

  protected HashMap<Integer, Node> _nodes;
  protected HashSet<Edge> _edges;
  protected double _evaporCoeff = 0.95;

  protected Graph() {
    _nodes = new HashMap<Integer, Node>();
    _edges = new HashSet<Edge>();
  }

  protected Graph(List<Integer> nodeIDs) {
    _nodes = new HashMap<Integer, Node>();
    _edges = new HashSet<Edge>();

    for(Integer id : nodeIDs) {
      _nodes.put(id, new Node(id));
    }
  }

  /**
   * List of nodes of graph .
   * 
   * @return - List of nodes
   */
  public HashMap<Integer, Node> getNodes() {
    return _nodes;
  }

  /**
   * List of edges of graph .
   * 
   * @return - List of edges
   */
  public HashSet<Edge> getEdges() {
    return _edges;
  }

  /**
   * Evaporating from each edges of graph.
   */
  public void evaporate() {
    for (Edge e : getEdges()) {
      e.evaporateFromEdge(_evaporCoeff);
    }
  }

  public void setEvaporCoeff(double evaporCoeff) {
    _evaporCoeff = evaporCoeff;
  }

  /**
   * .
   * @param newEdge .
   * @throws Exception .
   */
  public void addEdge(Edge newEdge) throws Exception {
    if (!_nodes.containsValue(newEdge.getNodes()[0])) {
      throw new Exception(
        "Graph does not contain the node with id: " + newEdge.getNodes()[0].getID());
    }
    if (!_nodes.containsValue(newEdge.getNodes()[1])) {
      throw new Exception(
        "Graph does not contain the node with id: " + newEdge.getNodes()[1].getID());
    }

    Node n1 = newEdge.getNodes()[0];
    n1.addEdge(newEdge);

    Node n2 = newEdge.getNodes()[1];
    n2.addEdge(newEdge);    

    _edges.add(newEdge);
  }

  /**
   * This method removes the edge from both graph and nodes.
   * @param edge .
   * @throws Exception .
   */
  public void removeEdge(Edge edge) throws Exception {
    // Remove the edge from edges
    this._edges.remove(edge);
    // Remove the edge from nodes
    Node n1 = edge.getNodes()[0];    
    n1.removeEdge(edge);
    Node n2 = edge.getNodes()[1];    
    n2.removeEdge(edge);
  }

  public void prune(long iteration) throws Exception {
    // this method is empty
  }

  /**
   * .
   */ 
  public static List<Node> edgeListToNodeList(List<Edge> edges) throws Exception {
    ArrayList<Node> nodeList = new ArrayList<Node>();

    // Edge previous = null;
    Node previous = null;
    for (int i = 0; i < edges.size(); i++) {
      Edge edge = edges.get(i);      
      if (previous == null) {  
        Edge nextEdge = edges.get(i + 1);
        
        if (edge.getNodes()[0] == nextEdge.getNodes()[0]) {
          previous = edge.getNodes()[1];
        }
        if (edge.getNodes()[0] == nextEdge.getNodes()[1]) {
          previous = edge.getNodes()[1];
        }

        if (edge.getNodes()[1] == nextEdge.getNodes()[0]) {
          previous = edge.getNodes()[0];
        }        
        if (edge.getNodes()[1] == nextEdge.getNodes()[1]) {
          previous = edge.getNodes()[0];
        }
        
        nodeList.add(previous);
      }
      Node next = edge.getNode2(previous);
      nodeList.add(next);
      previous = next;
    }
    return nodeList;
  }

  /**
   * .
   * @param edges .
   * @return .
   * @throws Exception .
   */
  public static List<Integer> edgeListToNodeIds(List<Edge> edges) throws Exception {
    return Graph.edgeListToNodeList(edges)
        .stream()
        .map((Node n) -> n.getID())
        .collect(Collectors.toList());
  }


  public List<Node> nodesToNodePath(List<Integer> nodes) {
    return nodes.stream().map(n -> _nodes.get(n)).toList();
  }
}
