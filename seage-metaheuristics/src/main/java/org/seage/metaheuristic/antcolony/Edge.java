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

/**
 *
 * @author Martin Zaloga
 */
public class Edge {
  private Node _node1;
  private Node _node2;
  private double _edgePrice;
  private double _pheromone;

  public Edge(Node start, Node end) throws Exception {
    _node1 = start;
    _node2 = end;
    _node1.addEdge(this);
    _node2.addEdge(this);
    _edgePrice = 0;
    _pheromone = 0;
  }

  /**
   * Pheromone on edge finding
   * 
   * @return - Value of pheromone
   */
  public double getLocalPheromone() {
    return _pheromone;
  }

  /**
   * Local pheromone addition
   * 
   * @param pheromone
   */
  public void addLocalPheromone(double pheromone) {
    _pheromone += pheromone;
  }

  /**
   * Pheromone evaporation
   */
  public void evaporateFromEdge(double evapoCoef) {
    _pheromone = _pheromone * evapoCoef;
    if (_pheromone < 0.00001) {
      _pheromone = 0.00001;
    }
  }

  /**
   * Edge length finding
   * 
   * @return - Edge length
   */
  public double getEdgePrice() {
    return _edgePrice;
  }

  /**
   * Edge length setting
   * 
   * @param length - Edge length
   */
  public void setEdgePrice(double length) {
    _edgePrice = length;
  }

  /**
   * Firs of nodes
   * 
   * @return - First node
   */
  public Node getNode2() {
    return _node2;
  }

  /**
   * Second of nodes
   * 
   * @return - Second node
   */
  public Node getNode1() {
    return _node1;
  }
}
