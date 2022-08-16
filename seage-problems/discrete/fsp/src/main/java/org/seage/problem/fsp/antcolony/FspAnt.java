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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
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
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.antcolony.JspAnt;
import org.seage.problem.jsp.antcolony.JspGraph;

/**
 * .
 * @author David Omrai
 */
public class FspAnt extends JspAnt {
  // Jobs ID permutation on the first machine
  protected int[] jobsOrder;
  protected int jobsOrderId;

  /**
   * .
   */
  public FspAnt(
      JspGraph graph, List<Integer> nodeIDs,
      JspJobsDefinition jobs,
      JspPhenotypeEvaluator evaluator
  ) {
    super(graph, nodeIDs, jobs, evaluator);

    jobsOrder = new int[this.jobsDefinition.getJobsCount()];
    for (int jobId = 0; jobId < jobsOrder.length; jobId++) {
      jobsOrder[jobId] = 0;
    }
    jobsOrderId = 0;
  }

  @Override
  protected void setNextStep(Edge step, Node lastNode) throws Exception {
    JspGraph jspGraph = (JspGraph)_graph;
    Node nextNode = step.getNode2(lastNode);
    if (lastNode == nextNode) {
      nextNode = lastNode;
    }
    lastJobOperations[jspGraph.nodeToJobID(nextNode) - 1]++;

    // Store job id if on the first machine
    if (jspGraph.nodeToOperID(nextNode) == 1) {
      jobsOrder[jobsOrderId++] = jspGraph.nodeToJobID(nextNode);
    }
  }


  /**
   * .
   * @param jobID .
   * @param operID .
   * @return
   */
  private boolean isOperationAvailable(int jobID, int operID) {
    // todo
    return false;
  }

  @Override
  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    // Clean the previous available nodes
    if (availableNodes == null) {
      availableNodes = new HashSet<Node>();
    } else {
      availableNodes.clear();
    }    

    JspGraph jspGraph = (JspGraph)_graph;
    // Crate new updated available nodes
    for (int jobIndex = 0; jobIndex < lastJobOperations.length; jobIndex++) {
      int jobID = jobIndex + 1;
      int operID = lastJobOperations[jobIndex] + 1;

      if (operID > jobsDefinition.getJobInfos()[jobIndex].getOperationInfos().length) {
        continue;
      }

      // the magic goes here
      if (!isOperationAvailable(jobID, operID)) {
        continue;
      }
      // end of magic

      int nodeID = jobID * jspGraph.getFactor() + operID;
      availableNodes.add(this._graph.getNodes().get(nodeID)); 
    }

    return availableNodes;
  }

}
