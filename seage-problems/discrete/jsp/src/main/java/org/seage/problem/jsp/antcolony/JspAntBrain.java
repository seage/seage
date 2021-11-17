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

import java.util.HashSet;
import java.util.List;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;

import org.seage.problem.jsp.JobsDefinition;

/**
 *
 * @author Zagy
 * Edited by David Omrai
 */
public class JspAntBrain extends AntBrain {
  JobsDefinition jobsDefinition;

  public JspAntBrain(Graph graph, JobsDefinition jobs) {
    super(graph);
    this.jobsDefinition = jobs;
  }

  //	@Override
  //	protected Node selectNextNode(Node firstNode, Node currentNode) 
  //	{		 
  // this part should look if the next one can be reached, if the noce isnt the precesesror of some that was before
  // thing is how to know what nodes were before, mayber array and there adding all other jobs are connected to all 
  // different...
  //		Node n = super.selectNextNode(firstNode, currentNode);
  //		if(n==null && firstNode != currentNode)
  //			return firstNode;
  //		else
  //			return n;
  //	}

  @Override
  public void reset() {
    availableNodes = null;
    availableNodeList.clear();
    // Clear used operations
  }

  @Override
  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    // Clean the previous available nodes
    this.availableNodes = new HashSet<>();

    //Create new available nodes
    for (int jobID = 1; jobID < this.jobsDefinition.getJobsCount(); jobID++) {
      for (int operID = 1; operID < this.jobsDefinition.getJobInfos()[jobID].getOperationInfos().length; operID++) {
        int nodeID = jobID * 100 + operID;
        Node nd = this.graph.getNodes().get(nodeID);

        if (!nodePath.contains(nd)) {
          this.availableNodes.add(nd);
          break;
        }
      }
    }

    return this.availableNodes;
  }

  @Override
  protected void markSelected(Node nextNode) {
    availableNodes.remove(nextNode);
  }

  protected int nodeToJobID(Node node) {
    return JspGraph.nodeToJobID(node);
  }

  protected int nodeToOperID(Node node) {
    return JspGraph.nodeToOperID(node);
  }

  //	@Override
  //	protected List<Node> getAvailableNodes(Node currentNode, HashSet<Node> visited) {
  //		
  //		int nextID = Math.abs(currentNode.getID())+1;
  //		if(!_graph.getNodes().containsKey(nextID))
  //			return null;
  //		ArrayList<Node> result = new ArrayList<Node>();
  //		result.add(_graph.getNodes().get(nextID));
  //		result.add(_graph.getNodes().get(-nextID));
  //		return result;
  //	}

  //  @Override
  //  protected Node selectNextNode(Node currentNode, List<Node> nodes, HashSet<Node> visited)
  //  {
  //    double[] probabilities = new double[edges.size()];
  //    double sum = 0;
  //    // for each Edges
  //    for (int i = 0; i < probabilities.length; i++) {
  //      Edge e = edges.get(i);
  //      probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgePrice(), _beta);
  //      sum += probabilities[i];
  //    }
  //    for (int i = 0; i < probabilities.length; i++) {
  //      probabilities[i] /= sum;
  //    }
  //    return edges.get(next(probabilities));
  //  }

  //  protected double pathCost(Vector<Edge> path) {
  //    Boolean[] solution = new Boolean[_formula.getLiteralCount()];
  //    Node node;
  //    for (int i = 0; i < _formula.getLiteralCount(); i++) {
  //      node = (Node) path.get(i).getNode2();
  //      if (node.getID() < 0) {
  //        solution[i] = false;
  //      } else {
  //        solution[i] = true;
  //      }
  //    }
  //    return (FormulaEvaluator.evaluate(_formula, solution) + 0.1);
  //  }
}
