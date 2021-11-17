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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.metaheuristic.antcolony.Edge;
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

  private JobsDefinition jobs;

  public JspGraph(JobsDefinition jobArary, JspPhenotypeEvaluator evaluator) throws Exception
  {
    super();
    this.evaluator = evaluator;
    this.jobs = jobArary;

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
        id = idJob * 100 + idOper;
        _nodes.put(id, new Node(id));
      }
    }
  }

  public static int nodeToJobID(Node node) {
    return node.getID() / 100;
  }

  public static int nodeToOperID(Node node) {
    return node.getID() % 100;
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
    Node start = nodePath.get(0);
    Node end = nodePath.get(nodePath.size() - 1);
    // If the first node is starting node
    if (start.getID() == 0)
      return 1;

    // ({prev timespan} - {curr timespan}) + 1

    // Get info about the start node
    int jobID = nodeToJobID(start);
    int operID = nodeToOperID(start);


    ArrayList<Integer> path = new ArrayList<>();
    getNodesPath(path, start, start);

   
    Integer[] prevPath = path.toArray(new Integer[0]);
    path.add(nodeToJobID(end));
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

  public void getNodesPath(List<Integer> path, Node node, Node prevNode) {
    if (node.getID() == 0 )
      return;

    Iterator<Edge> iter = node.getEdges().iterator();
    Edge first = iter.next();

    if (first.getNode2().getID() == node.getID()) {
      getNodesPath(path, first.getNode1(), node);
    }
    else {
      Edge second = iter.next();
      if (second.getNode2().getID() != node.getID())
        return;
      getNodesPath(path, second.getNode1(), node);
    }
    
    path.add(nodeToJobID(node));
  }
}
