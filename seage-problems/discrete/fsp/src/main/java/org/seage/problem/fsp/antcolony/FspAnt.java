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

package org.seage.problem.fsp.antcolony;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.seage.metaheuristic.antcolony.Ant;
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
public class FspAnt extends Ant {
  private static final Logger log = LoggerFactory.getLogger(FspAnt.class.getName());

  protected JspPhenotypeEvaluator evaluator;
  protected JspJobsDefinition jobsDefinition;

  /**
   * .
   * @param initNodePath .
   * @param jobs .
   * @param evaluator .
   */
  public FspAnt(
      List<Node> initNodePath,
      JspJobsDefinition jobs, 
      JspPhenotypeEvaluator evaluator
  ) {
    super(initNodePath);
    this.jobsDefinition = jobs;
    this.evaluator = evaluator;
  }

  @Override
  protected HashSet<Node> getAvailableNodes(Graph graph, List<Node> nodePath) {
    HashSet<Node> availableNodes = new HashSet<>(graph.getNodes().values());

    for (Node n : nodePath) {
      availableNodes.remove(n);
    }

    return availableNodes;
  }

  /**
   * Edge length calculating.
   */
  @Override
  public double getNodeDistance(Graph graph, List<Node> nodePath, Node node) {
    Node end = nodePath.get(nodePath.size() - 1);
    // If the first node is starting node
    if (end.getID() == 0) {
      return 1;
    }

    // Copy the path to all machines
    ArrayList<Integer> path = new ArrayList<>();
    for (Node n : nodePath.subList(1, nodePath.size())) {
      for (int m = 0; m < jobsDefinition.getMachinesCount(); m++) {
        path.add(n.getID());        
      }
    }

    Integer[] prevPath = path.toArray(new Integer[0]);
    // Add the new node to each machine
    for (int m = 0; m < jobsDefinition.getMachinesCount(); m++) {
      path.add(node.getID());          
    }
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
}
