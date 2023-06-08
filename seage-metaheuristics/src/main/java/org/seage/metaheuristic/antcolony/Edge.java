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

/**
 *
 * @author Martin Zaloga
 */
public class Edge {
  private Node _node1;
  private Node _node2;
  private double _edgePrice;
  private double _pheromone;

  public Edge(Node node1, Node node2, double edgePrice) {
    _node1 = node1;
    _node2 = node2;
    _edgePrice = edgePrice;
    _pheromone = 0;
  }

  /**
   * Pheromone on edge finding.
   * 
   * @return - Value of pheromone
   */
  public double getLocalPheromone() {
    return _pheromone;
  }

  /**
   * Local pheromone addition.
   * 
   * @param pheromone
   */
  public void addLocalPheromone(double pheromone) {
    _pheromone += pheromone;
  }

  /**
   * Pheromone evaporation.
   */
  public void evaporateFromEdge(double evapoCoef) {
    _pheromone = _pheromone * evapoCoef;
    if (_pheromone < 0.00001) {
      _pheromone = 0.00001;
    }
  }

  /**
   * Edge length finding.
   * 
   * @return - Edge length
   */
  public double getEdgePrice() {
    return _edgePrice;
  }

  /**
   * Edge length setting.
   * 
   * @param edgePrice - Edge length
   */
  public void setEdgePrice(double edgePrice) {
    _edgePrice = edgePrice;
  }

  /**
   * Get the second of nodes.
   * 
   * @throws Exception
   */
  public Node getNode2(Node node1) throws Exception {
    if(node1 != _node1 && node1 != _node2)
      throw new Exception("Node is not related to the edge, id: " + node1.getID());
    Node result = _node2;
    if(result == node1)
      result = _node1;
    return result;
  }

  /**
   * Get edge nodes.
   * 
   * @return - Both nodes
   */
  public Node[] getNodes() {
    return new Node[] {_node1, _node2};
  }

  public String toString() {
    return String.format("%d->%d(%f, %f)", _node1.getID(), _node2.getID(), getEdgePrice(), getLocalPheromone());
  }

  @Override
  public int hashCode() {
    int a = this._node1.getID();
    int b = this._node2.getID();
    return a >= b ? a * a + a + b : a + b * b;
  } 
}
