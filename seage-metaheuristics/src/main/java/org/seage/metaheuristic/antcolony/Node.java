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
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.metaheuristic.antcolony;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Node class.
 * @author Martin Zaloga
 */
public class Node {

  private int id;
  private HashMap<Node, Edge> edges;
  private HashMap<Integer, Node> nodes;

  /** Node. */
  public Node(int id) {
    this.id = id;
    edges = new HashMap<>();
    nodes = new HashMap<>();
  }

  /**
   * Identification number.
   * 
   * @return - Number id
   */
  public int getID() {
    return id;
  }

  /**
   * Edge in edge-list adding.
   * 
   * @param edge - Edge for add
   * @throws Exception ex
   */
  public void addEdge(Edge edge) throws Exception {
    Node node2 = edge.getNode2(this); // Node to link
    Node node1 = edge.getNode2(node2);

    if (node1.equals(node2)) {
      throw new IllegalArgumentException("Edge with both nodes the same.");
    }
    if (!(node1.equals(this) || node2.equals(this))) {
      throw new IllegalArgumentException("The adding edge is not related to the current node.");
    }


    if (!edges.containsValue(edge)) {
      edges.put(node2, edge);
    }

    nodes.put(node2.getID(), node2);

  }

  /**
   * Removes the given edge from edges
   * @param edge
   */
  public void removeEdge(Edge edge) throws Exception {
    Node node1 = this;
    Node node2 = edge.getNode2(node1);

    if (node1.equals(node2)) {
      throw new IllegalArgumentException("Edge with both nodes the same.");
    }
    if (edges.containsValue(edge)) {
      this.edges.remove(node2);
      this.nodes.remove(node2.getID());
    }
  }

  /**
   * List all edges which are joined with actual node.
   * 
   * @return - List edges
   */
  public Collection<Edge> getEdges() {
    return Collections.unmodifiableCollection(edges.values());
  }

  public Edge getEdge(Node n) {
    return edges.get(n);
  }

  @Override
  public int hashCode() {
    return this.id;
  }

  /**
   * My implementation function equals.
   * 
   * @param node - Compared node
   * @return - if compared nodes are some
   */
  public boolean equals(Object node) {
    if (node instanceof Node) {
      Node n = (Node) node;
      return n.id == this.id;
    }
    return false;    
  }

  @Override
  public String toString() {
    return String.valueOf(this.id);
  }
}
