/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *   Richard Malek
 *   - Initial implementation
 */
package org.seage.problem.jsp.antcolony;

import java.util.Arrays;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 *
 * @author Zagy
 */
public class JspAntColonySolution extends Graph
{

  JspPhenotypeEvaluator evaluator;

  private Integer[] jobArray;

  public JspAntColonySolution(Integer[] jobArary, JspPhenotypeEvaluator evaluator) throws Exception
  {
    super();
    this.jobArray = jobArary;
    this.evaluator = evaluator;
    for (int id = 1; id < jobArary.length; id++) {
      _nodes.put(id, new Node(id));
    }
  }

  //	@Override
  //	public List<Node> getAvailableNodes(Node startingNode, Node currentNode, HashSet<Node> visited)
  //	{
  //		List<Node> result = super.getAvailableNodes(startingNode, currentNode, visited);
  //		if(currentNode != startingNode && visited.size() == getNodes().values().size())
  //		{
  //			result = new ArrayList<Node>();
  //			result.add(startingNode);
  //		}
  //		return result;
  //	}
  /**
   * Edge length calculating
   * @param start - Starting node
   * @param end - Terminate node
   * @param cities - Readed cities
   * @return - Euclide edge length
   */
  @Override
  public double getNodesDistance(Node start, Node end)
  {
    try {
      double[] res = this.evaluator.evaluateSchedule(Arrays.copyOfRange(this.jobArray, 0, end.getID())); 
      return res[0];
    } catch (Exception e) {
      e.printStackTrace();
      return 0.0;
    }
  }
}
