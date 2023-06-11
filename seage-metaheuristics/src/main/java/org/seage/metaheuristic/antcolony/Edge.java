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
  private Node node1;
  private Node node2;
  private double edgeCost;
  private double pheromone;

  public Edge(Node node1, Node node2, double edgeCost) {
    this.node1 = node1;
    this.node2 = node2;
    this.edgeCost = edgeCost;
    this.pheromone = 0;
  }

  /**
   * Pheromone on edge finding.
   * 
   * @return - Value of pheromone
   */
  public double getLocalPheromone() {
    return pheromone;
  }

  /**
   * Local pheromone addition.
   * 
   * @param pheromone
   */
  public void addLocalPheromone(double pheromone) {
    this.pheromone += pheromone;
  }

  /**
   * Pheromone evaporation.
   */
  public void evaporateFromEdge(double evapoCoef) {
    pheromone = pheromone * evapoCoef;
    if (pheromone < 0.00001) {
      pheromone = 0.00001;
    }
  }

  /**
   * Edge length finding.
   * 
   * @return - Edge length
   */
  public double getEdgeCost() {
    return edgeCost;
  }

  /**
   * Edge length setting.
   * 
   * @param edgeCost - Edge length
   */
  public void setEdgeCost(double edgeCost) {
    this.edgeCost = edgeCost;
  }

  /**
   * Get the second of nodes.
   * 
   * @throws Exception
   */
  public Node getNode2(Node node1) throws Exception {
    if (node1 != this.node1 && node1 != this.node2) {
      throw new Exception("Node is not related to the edge, id: " + node1.getID());
    }
    Node result = this.node2;
    if (result == node1) {
      result = this.node1;
    }
    return result;
  }

  /**
   * Get edge nodes.
   * 
   * @return - Both nodes
   */
  public Node[] getNodes() {
    return new Node[] {node1, node2};
  }

  @Override
  public int hashCode() {
    int x = node1.getID();
    int y = node2.getID();
    return x < y ? y * y + x + y : x * x + x + y;
  }

  @Override
  public boolean equals(Object obj) {
    return obj != null ? this.hashCode() == obj.hashCode() : false;
  }

  public String toString() {
    return String.format("%d->%d(%f, %f)", 
        node1.getID(), node2.getID(), getEdgeCost(), getLocalPheromone());
  }
}
