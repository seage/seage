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
import java.util.HashSet;
import java.util.List;

import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 *
 * @author Zagy
 * Edited by David Omrai
 */
public class JspAntBrain extends AntBrain {
  JspPhenotypeEvaluator evaluator;
  JobsDefinition jobsDefinition;
  JspGraph jspGraph;

  public JspAntBrain(JspGraph graph, JobsDefinition jobs, JspPhenotypeEvaluator evaluator) {
    super(graph);
    this.jspGraph = graph;
    this.jobsDefinition = jobs;
    this.evaluator = evaluator;
  }

   @Override
  protected Edge selectNextStep(List<Node> nodePath) throws Exception {
    return super.selectNextStep(nodePath);
  }

  @Override
  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    // Clean the previous available nodes
    var availableNodes = new HashSet<Node>();

    //Create new available nodes
    for (int jobID = 1; jobID <= this.jobsDefinition.getJobsCount(); jobID++) {
      for (int operID = 1; operID <= this.jobsDefinition.getJobInfos()[jobID-1].getOperationInfos().length; operID++) {
        int nodeID = jobID * jspGraph.getFactor() + operID;
        Node nd = this.graph.getNodes().get(nodeID);

        if (!nodePath.contains(nd)) {
          availableNodes.add(nd);
          break;
        }
      }
    }

    return availableNodes;
  }


  /**
   * Edge length calculating
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
      path.add(jspGraph.nodeToJobID(n));
   
    Integer[] prevPath = path.toArray(new Integer[0]);
    path.add(jspGraph.nodeToJobID(node));
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


  @Override
  public double getPathCost(List<Edge> path) {
    var nodes = Graph.edgeListToNodeList(path);
    Integer[] jobArray = new Integer[nodes.size()-1];
    for(int i=1;i<nodes.size();i++)
      jobArray[i-1] = nodes.get(i).getID() / jspGraph.getFactor();

    try {
      return evaluator.evaluateSchedule(jobArray);
    } catch (Exception e) {
      return 1.0;
    }
  }
}