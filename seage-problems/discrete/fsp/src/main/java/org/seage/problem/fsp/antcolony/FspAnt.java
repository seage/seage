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
import java.util.Arrays;
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
public class FspAnt extends Ant {
  private static final Logger log = LoggerFactory.getLogger(FspAnt.class.getName());

  protected JspPhenotypeEvaluator evaluator;
  protected JspJobsDefinition jobsDefinition;

  /**
   * .
   * @param graph .
   * @param nodeIDs .
   * @param jobs .
   * @param evaluator .
   */
  public FspAnt(
      FspGraph graph, 
      List<Integer> nodeIDs,
      JspJobsDefinition jobs, 
      JspPhenotypeEvaluator evaluator
  ) {
    super(graph, nodeIDs);
    this.jobsDefinition = jobs;
    this.evaluator = evaluator;
    _nodePath.add(0, _graph.getNodes().get(0));

  }

  @Override
  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    //HashSet<Node> availableNodes = super.getAvailableNodes(nodePath);
    // Node startingNode = nodePath.get(0);
    // Node currentNode = nodePath.get(nodePath.size() - 1);
    // System.out.println("start of this one");
    // for (Node n : nodePath) {
    //   System.out.println(n.getID());
    // }

    // System.out.println("-------------");

    // for (Node n : availableNodes) {
    //   System.out.println(n.getID());
    // }
    // // if (currentNode != startingNode && availableNodes.size() == 0) {
    // //   availableNodes.add(startingNode);
    // // }

    HashSet<Node> availableNodes = new HashSet<>(_graph.getNodes().values());

    for (Node n : nodePath) {
      availableNodes.remove(n);
    }

    return availableNodes;
  }

  /**
   * Edge length calculating.
   */
  @Override
  public double getNodeDistance(List<Node> nodePath, Node node) {
    Node end = nodePath.get(nodePath.size() - 1);
    // If the first node is starting node
    if (end.getID() == 0) {
      return 1;
    }

    // System.out.println("begin-----------------\n-> " + (end.getID()));

    // Copy the path to all machines
    ArrayList<Integer> pathOld = new ArrayList<>();
    ArrayList<Integer> pathNew = new ArrayList<>();
    
      // Sublist to jump over the first 0 staring node
    for (Node n : nodePath.subList(1, nodePath.size())) {
      for (int m = 0; m < jobsDefinition.getMachinesCount(); m++) {
        pathOld.add(n.getID());
        pathNew.add(n.getID());          
      }
    }
    for (int m = 0; m < jobsDefinition.getMachinesCount(); m++) {
      pathNew.add(node.getID());          
    }



    Integer[] prevPath = pathOld.toArray(new Integer[0]);
    Integer[] nextPath = pathNew.toArray(new Integer[0]);

    // System.out.println("---");
    // System.out.println(Arrays.toString(prevPath));
    // System.out.println(Arrays.toString(nextPath));

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