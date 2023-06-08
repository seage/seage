/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
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
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Zagy
 * @author David Omrai
 */
public class JspAnt extends Ant {
  private static final Logger log = LoggerFactory.getLogger(JspAnt.class.getName());

  protected JspPhenotypeEvaluator evaluator;
  protected JspJobsDefinition jobsDefinition;

  // For faster performing of the getAvailableNodes() method
  protected int[] lastJobOperations;

  /**
   * .
   * @param initialPath .
   * @param jobs .
   * @param evaluator .
   */
  public JspAnt(
      List<Integer> initialPath,
      JspJobsDefinition jobs, 
      JspPhenotypeEvaluator evaluator
  ) {
    super(initialPath);
    this.jobsDefinition = jobs;
    this.evaluator = evaluator;

    lastJobOperations = new int[this.jobsDefinition.getJobsCount()];
    for (int jobIndex = 0; jobIndex < lastJobOperations.length; jobIndex++) {
      lastJobOperations[jobIndex] = 0;
    }
  }

  protected void setNextStep(JspGraph graph, Edge step, Node lastNode) throws Exception {
    // Increase the operation
    Node nextNode = step.getNode2(lastNode);
    if (lastNode == nextNode) {
      nextNode = lastNode;
    }
    lastJobOperations[graph.nodeToJobID(nextNode) - 1]++;
  }

  @Override
  protected Edge selectNextStep(Graph graph, List<Node> nodePath) throws Exception {
    Edge nextEdge = super.selectNextStep(graph, nodePath);
    if (nextEdge != null) {
      setNextStep((JspGraph) graph, nextEdge, nodePath.get(nodePath.size() - 1));
    }
    return nextEdge;
  }

  @Override
  protected HashSet<Node> getAvailableNodes(Graph graph, List<Node> nodePath) {
    HashSet<Node> availableNodes = new HashSet<Node>();
   

    JspGraph jspGraph = (JspGraph) graph;
    // Crate new updated available nodes
    for (int jobIndex = 0; jobIndex < lastJobOperations.length; jobIndex++) {
      int jobID = jobIndex + 1;
      int operID = lastJobOperations[jobIndex] + 1;

      if (operID > jobsDefinition.getJobInfos()[jobIndex].getOperationInfos().length) {
        continue;
      }

      int nodeID = jobID * jspGraph.getFactor() + operID;
      availableNodes.add(graph.getNodes().get(nodeID));
    }

    return availableNodes;
  }

  @Override
  protected List<Edge> explore(Graph graph, Node startingNode) throws Exception {
    // Clean the array for new exploration
    for (int jobIndex = 0; jobIndex < lastJobOperations.length; jobIndex++) {
      lastJobOperations[jobIndex] = 0;
    }

    return super.explore(graph, startingNode);
  }

  /**
   * Edge length calculating.
   */
  @Override
  public double getNodeDistance(Graph graph, List<Node> nodePath, Node node) {
    JspGraph jspGraph = (JspGraph) graph;
    Node end = nodePath.get(nodePath.size() - 1);
    // If the first node is starting node
    if (end.getID() == 0) {
      return 1;
    }

    ArrayList<Integer> path = new ArrayList<>();
    for (Node n : nodePath.subList(1, nodePath.size())) {
      path.add(jspGraph.nodeToJobID(n));
    }

    Integer[] prevPath = path.toArray(new Integer[0]);
    path.add(jspGraph.nodeToJobID(node));
    Integer[] nextPath = path.toArray(new Integer[0]);

    double prevTimespan = 0;
    double nextTimespan = 0;
    try {
      prevTimespan = this.evaluator.evaluateSchedule(prevPath);
      nextTimespan = this.evaluator.evaluateSchedule(nextPath);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }

    // Get the start node operation length and add one, for the step to another node
    return nextTimespan - prevTimespan + 1.0;
  }


  @Override
  public double getPathCost(Graph graph, List<Edge> path) throws Exception {
    JspGraph jspGraph = (JspGraph) graph;
    var nodes = Graph.edgeListToNodeList(path);
    Integer[] jobArray = new Integer[nodes.size() - 1];
    for (int i = 1; i < nodes.size(); i++) {
      jobArray[i - 1] = nodes.get(i).getID() / jspGraph.getFactor();
    }

    try {
      return evaluator.evaluateSchedule(jobArray);
    } catch (Exception e) {
      return 1.0;
    }
  }
}