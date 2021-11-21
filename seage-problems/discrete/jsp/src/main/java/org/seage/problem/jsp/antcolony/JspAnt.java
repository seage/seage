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
import org.seage.metaheuristic.antcolony.Ant;
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
public class JspAnt extends Ant {
  JspPhenotypeEvaluator evaluator;
  JobsDefinition jobsDefinition;

  // For faster performing of the getAvailableNodes() method
  private int[] lastJobOperations;

  public JspAnt(JspGraph graph, List<Integer> nodeIDs, JobsDefinition jobs, JspPhenotypeEvaluator evaluator) {
    super(graph, nodeIDs);
    this.jobsDefinition = jobs;
    this.evaluator = evaluator;

    lastJobOperations = new int[this.jobsDefinition.getJobsCount()];
    for (int jobIndex = 0; jobIndex < lastJobOperations.length; jobIndex++)
      lastJobOperations[jobIndex] = 0;
  }

  protected void setNextStep(Edge step) {
    // Increase the operation
    JspGraph jspGraph = (JspGraph)_graph;
    lastJobOperations[jspGraph.nodeToJobID(step.getNode2())-1]++;
  }

   @Override
  protected Edge selectNextStep(List<Node> nodePath) throws Exception {
    Edge nextEdge = super.selectNextStep(nodePath);
    setNextStep(nextEdge);   
    return nextEdge;
  }

  @Override
  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    // Clean the previous available nodes
    availableNodes.clear();

    JspGraph jspGraph = (JspGraph)_graph;
    // Crate new updated available nodes
    for (int jobIndex = 0; jobIndex < lastJobOperations.length; jobIndex++) {
      int jobID = jobIndex + 1;
      int operID = lastJobOperations[jobIndex] + 1;

      if (operID > jobsDefinition.getJobInfos()[jobIndex].getOperationInfos().length)
        continue;

      int nodeID = jobID * jspGraph.getFactor() + operID;
      availableNodes.add(this._graph.getNodes().get(nodeID)); 
    }

    return availableNodes;
  }

  @Override
  protected List<Edge> explore(Node startingNode) throws Exception {
    // Clean the array for new exploration
    lastJobOperations = new int[this.jobsDefinition.getJobsCount()];
    for (int jobIndex = 0; jobIndex < lastJobOperations.length; jobIndex++)
      lastJobOperations[jobIndex] = 0;

    return super.explore(startingNode);
  }

  /**
   * Edge length calculating
   */
  @Override
  public double getNodeDistance(List<Node> nodePath, Node node) {
    JspGraph jspGraph = (JspGraph)_graph;
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
    JspGraph jspGraph = (JspGraph)_graph;
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