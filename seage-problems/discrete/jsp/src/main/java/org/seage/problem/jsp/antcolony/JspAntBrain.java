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
import org.seage.problem.jsp._old.JspEvaluator_old;

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
  protected Node selectNextNode(List<Node> nodePath) {
    return super.selectNextNode(nodePath);
  }

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
    for (int jobID = 1; jobID <= this.jobsDefinition.getJobsCount(); jobID++) {
      for (int operID = 1; operID <= this.jobsDefinition.getJobInfos()[jobID-1].getOperationInfos().length; operID++) {
        int nodeID = jobID * jspGraph.getFactor() + operID;
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
    return this.jspGraph.nodeToJobID(node);
  }

  protected int nodeToOperID(Node node) {
    return this.jspGraph.nodeToOperID(node);
  }


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
  //  protected Node selectNextNode(List<Node> nodePath) {
  //   Node currentNode = nodePath.get(nodePath.size()-1);
  //   HashSet<Node> nextAvailableNodes = getAvailableNodes(nodePath);
  //   availableNodeList.clear();

  //   if (nextAvailableNodes == null || nextAvailableNodes.isEmpty()) {
  //     return null;
  //   }

  //   double sum = 0;
  //   int i = 0;
  //   double[] probabilities = new double[nextAvailableNodes.size()];
  //   // for each available node calculate probability
  //   for (Node n : nextAvailableNodes) {
  //     double edgePheromone = 0;
  //     double edgePrice = 0;

  //     Edge e = currentNode.getEdgeMap().get(n);
  //     if (e != null) {
  //       edgePheromone = e.getLocalPheromone();
  //       edgePrice = e.getEdgePrice();
  //     } else {
  //       edgePheromone = graph.getDefaultPheromone();
  //       edgePrice = graph.getNodeDistance(nodePath, n);
  //     }

  //     double p = pow(edgePheromone, alpha) * pow(1 / edgePrice, beta);
  //     probabilities[i] = p;
  //     availableNodeList.add(n);
  //     sum += p;
  //     i++;
  //   }

  //   sum = sum != 0 ? sum : 1;    
  //   for (i = 0; i < probabilities.length; i++) {
  //     probabilities[i] /= sum;
  //   }

  //   Node nextNode = availableNodeList.get(next(probabilities));
  //   markSelected(nextNode);

  //   return nextNode;
  // }

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
