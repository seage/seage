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
 */
package org.seage.problem.tsp.antcolony;

import java.util.HashSet;
import java.util.List;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 *
 * @author Zagy
 */
public class TspAntBrain extends AntBrain {
  City[] cities;
  public TspAntBrain(Graph graph, City[] cities) {
    super(graph);
    this.cities = cities;
  }

  @Override
  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    Node startingNode = nodePath.get(0);
    Node currentNode = nodePath.get(nodePath.size()-1);
    HashSet<Node> result = super.getAvailableNodes(nodePath);
    if (currentNode != startingNode && result.size() == 0) {
      result.add(startingNode);
    }
    return result;
  }

  /**
   * Edge length calculating
   * 
   * @param start  - Starting node
   * @param end    - Terminate node
   * @param cities - Readed cities
   * @return - Euclide edge length
   */
  @Override
  public double getNodeDistance(List<Node> nodePath, Node end) {
    Node start = nodePath.get(0);
    double dX = (this.cities[start.getID() - 1].X - this.cities[end.getID() - 1].X);
    double dY = (this.cities[start.getID() - 1].Y - this.cities[end.getID() - 1].Y);
    return Math.round(Math.sqrt(dX * dX + dY * dY));
  }
}
