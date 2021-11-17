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

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 *
 * @author Zagy
 * Edited by David Omrai
 */
public class JspGraph extends Graph
{
  JspPhenotypeEvaluator evaluator;

  private int factor;

  private JobsDefinition jobs;

  public JspGraph(JobsDefinition jobArary, JspPhenotypeEvaluator evaluator) throws Exception
  {
    super();
    this.evaluator = evaluator;
    this.jobs = jobArary;
    int allOperCount = 0;
    for (int i = 0; i < this.jobs.getJobsCount(); i++)
      allOperCount += this.jobs.getJobInfos()[i].getOperationInfos().length;

    this.factor = (int) Math.pow(10, Math.ceil(Math.log10(allOperCount)));

    // Add the first starting node
    //   /- 1 - 2 - 3 - ... - job1-oper '|
    //  /-  1 - 2 - 3 - ... - job2-oper  |
    // s -  1 - 2 - 3 - ... - job3-oper  | n-jobs
    //  \-  1 - 2 - 3 - ... - job4-oper  |
    //   \- 1 - 2 - 3 - ... - job5-oper ,|
    int id = 0;
    // Starting node
    _nodes.put(id, new Node(id));

    for (int idJob = 1; idJob <= jobs.getJobsCount(); idJob++) {
      for (int idOper = 1; idOper <= jobs.getJobInfos()[idJob-1].getOperationInfos().length; idOper++ ){
        id = idJob * getFactor() + idOper;
        _nodes.put(id, new Node(id));
      }
    }
  }

  public int nodeToJobID(Node node) {
    return node.getID() / getFactor();
  }

  public int nodeToOperID(Node node) {
    return node.getID() % getFactor();
  }

  public int getFactor() {
    return this.factor;
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
  public double getNodeDistance(List<Node> nodePath, Node node)
  {
    Node end = nodePath.get(nodePath.size() - 1);
    // If the first node is starting node
    if (end.getID() == 0)
      return 1;

    ArrayList<Integer> path = new ArrayList<>();
    for (Node n : nodePath.subList(1, nodePath.size())) 
      path.add(nodeToJobID(n));
   
    Integer[] prevPath = path.toArray(new Integer[0]);
    path.add(nodeToJobID(node));
    Integer[] nextPath = path.toArray(new Integer[0]);

    double prevTimespan = 0;
    double nextTimespan = 0;
    try {
      prevTimespan = this.evaluator.evaluateSchedule(prevPath);
      nextTimespan = this.evaluator.evaluateSchedule(nextPath);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Get the start node operation length and add one, for the step to another node
    return nextTimespan - prevTimespan + 1.0;
  }
}
