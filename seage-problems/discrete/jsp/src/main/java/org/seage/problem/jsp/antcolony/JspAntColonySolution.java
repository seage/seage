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
import java.lang.Math;

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
    this.jobs = jobArary;


    int jobsNum = jobs.getJobsCount();
    int factor = 0;
    for (int i = 0; i < jobs.getJobInfos().length; i++) {
      int curFactor = jobs.getJobInfos()[i].getOperationInfos().length;
      if (curFactor > factor) 
        factor = curFactor;
    }

    this.jobsArray = new int[jobsNum * factor + 1][2];

    // Add the first starting node
    //  /- 1 - 2 - 3 - ... - n-oper '|
    // /-  1 - 2 - 3 - ... - n-oper  |
    //s -  1 - 2 - 3 - ... - n-oper  | n-jobs
    // \-  1 - 2 - 3 - ... - n-oper  |
    //  \- 1 - 2 - 3 - ... - n-oper ,|
    int id = 0;
    for (int idJob = 0; idJob < jobsNum; idJob++) {
      for (int idOper = 0; idOper < factor; idOper++ ){
        _nodes.put(id, new Node(id));
        // Store informations about node
        this.jobsArray[id][0] = idJob;
        this.jobsArray[id][1] = idOper;
        id += 1;
      }
    }
    _nodes.put(id, new Node(id));
  }

  public int nodeToJobID(Node node) {
    return this.jobsArray[node.getID()][0];
  }

  public int nodeToOperID(Node node) {
    return this.jobsArray[node.getID()][1];
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
    // If the first node is starting node
    if (start.getID() == this.jobsArray.length)
      return 0;

    // ({prev timespan} - {curr timespan}) + 1

    // Get info about the start node
    int jobID = this.jobsArray[start.getID()][0];
    int operID = this.jobsArray[start.getID()][1];

    

    // Get the start node operation length and add one, for the step to another node
    return jobs.getJobInfos()[jobID].getOperationInfos()[operID].Length + 1.0;
  }
}
