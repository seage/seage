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

import java.util.List;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 *
 * @author Zagy
 */
public class TspGraph extends Graph {

  private City[] _cities;

  public TspGraph(City[] cities) throws Exception {
    super();
    _cities = cities;
    for (int id = 1; id <= cities.length; id++) {
      _nodes.put(new Integer(id), new Node(id));
    }
  }

  // @Override
  // public List<Node> getAvailableNodes(Node startingNode, Node currentNode,
  // HashSet<Node> visited)
  // {
  // List<Node> result = super.getAvailableNodes(startingNode, currentNode,
  // visited);
  // if(currentNode != startingNode && visited.size() ==
  // getNodes().values().size())
  // {
  // result = new ArrayList<Node>();
  // result.add(startingNode);
  // }
  // return result;
  // }
}
