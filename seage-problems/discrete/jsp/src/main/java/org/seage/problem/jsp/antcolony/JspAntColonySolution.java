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
 *   David Omrai
 *   - Jsp implementation
 */
package org.seage.problem.jsp.antcolony;

import java.util.Arrays;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 *
 * @author Zagy
 * Edited by David Omrai
 */
public class JspAntColonySolution extends Graph
{
  JspPhenotypeEvaluator evaluator;

  private JobsDefinition jobs;

  private int[][] jobsArray;

  public JspAntColonySolution(JobsDefinition jobArary, JspPhenotypeEvaluator evaluator) throws Exception
  {
    super();
    this.evaluator = evaluator;

    int operNum = jobs.getJobInfos()[0].getOperationInfos().length;

    this.jobsArray = new int[jobs.getJobsCount() * operNum][2];

    int id = 1;
    for (int idJob = 0; idJob < jobs.getJobsCount(); idJob++) {
      for (int idOper = 0; idOper < operNum; idOper++ ){
        _nodes.put(id, new Node(id));
        // Store informations about node
        this.jobsArray[id - 1][0] = idJob;
        this.jobsArray[id - 1][1] = idOper;
        id += 1;
      }
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
   * @return - time for ant to get from start to end
   */
  @Override
  public double getNodesDistance(Node start, Node end)
  {
    // Get info about the start node
    int jobID = this.jobsArray[start.getID()][0];
    int operID = this.jobsArray[start.getID()][1];
    // Get the start node operation length and add one, for the step to another node
    return jobs.getJobInfos()[jobID].getOperationInfos()[operID].Length + 1.0;
  }
}
