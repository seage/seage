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
 */

package org.seage.problem.tsp.antcolony;

import java.util.HashSet;
import java.util.List;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 * Tsp ant implementation.
 *
 * @author Zagy
 */
public class TspAnt extends Ant {
  City[] cities;
  
  public TspAnt(Graph graph, List<Integer> nodeIDs, City[] cities) {
    super(graph, nodeIDs);
    this.cities = cities;
  }

  @Override
  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    HashSet<Node> result = super.getAvailableNodes(nodePath);
    Node startingNode = nodePath.get(0);
    Node currentNode = nodePath.get(nodePath.size() - 1);
    if (currentNode != startingNode && result.isEmpty()) {
      result.add(startingNode);
    }
    return result;
  }

  /**
   * Edge length calculating.
   *
   * @param nodePath  Path for the ant
   * @param end   Terminate node
   * @return Euclidean edge length
   */
  @Override
  public double getNodeDistance(List<Node> nodePath, Node end) {
    Node start = nodePath.get(nodePath.size() - 1);
    double dx = (this.cities[start.getID() - 1].X - this.cities[end.getID() - 1].X);
    double dy = (this.cities[start.getID() - 1].Y - this.cities[end.getID() - 1].Y);
    return Math.round(Math.sqrt(dx * dx + dy * dy));
  }
}
