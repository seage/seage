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
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     Martin Zaloga
 *     - Reimplementation
 */

package org.seage.metaheuristic.antcolony;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * AntBrain class.
 * @author Martin Zaloga
 */
public class AntBrain {

  protected double alpha;
  protected double beta;
  protected double quantumPheromone;
  private Random rand;
  protected Graph graph;
  protected HashSet<Node> availableNodes;
  protected ArrayList<Node> availableNodeList;

  public AntBrain(Graph graph) {
    this.graph = graph;
    rand = new Random(System.currentTimeMillis());
    availableNodeList = new ArrayList<>();
  }

  void setParameters(double alpha, double beta, double quantumPheromone) {
    this.alpha = alpha;
    this.beta = beta;
    this.quantumPheromone = quantumPheromone;
  }

  public void reset() {
    availableNodes = null;
    availableNodeList.clear();
  }

  /**
   * Selection following edge.
   * 
   * @param edges   - Available edges
   * @param visited - Visited nodes
   * @return - Selected edge
   */
  protected Node selectNextNode(List<Integer> nodeIDsAlongPath) {
    Node currentNode = graph.getNodes().get(nodeIDsAlongPath.get(nodeIDsAlongPath.size()-1));
    HashSet<Node> nextAvailableNodes = getAvailableNodes(nodeIDsAlongPath);
    availableNodeList.clear();

    if (nextAvailableNodes == null || nextAvailableNodes.isEmpty()) {
      return null;
    }

    double sum = 0;
    int i = 0;
    double[] probabilities = new double[nextAvailableNodes.size()];
    // for each available node calculate probability
    for (Node n : nextAvailableNodes) {
      double edgePheromone = 0;
      double edgePrice = 0;

      Edge e = currentNode.getEdgeMap().get(n);
      if (e != null) {
        edgePheromone = e.getLocalPheromone();
        edgePrice = e.getEdgePrice();
      } else {
        edgePheromone = graph.getDefaultPheromone();
        edgePrice = graph.getNodesDistance(currentNode, n);
      }

      double p = pow(edgePheromone, alpha) * pow(1 / edgePrice, beta);
      probabilities[i] = p;
      availableNodeList.add(n);
      sum += p;
      i++;
    }

    sum = sum != 0 ? sum : 1;    
    for (i = 0; i < probabilities.length; i++) {
      probabilities[i] /= sum;
    }

    Node nextNode = availableNodeList.get(next(probabilities));
    markSelected(nextNode);

    return nextNode;
  }

  protected HashSet<Node> getAvailableNodes(List<Integer> nodeIDsAlongPath) {
    if (availableNodes == null) {
      Node firstNode = graph.getNodes().get(nodeIDsAlongPath.get(0));
      availableNodes = new HashSet<>(graph.getNodes().values());
      availableNodes.remove(firstNode);
    }
    return availableNodes;
  }

  protected void markSelected(Node nextNode) {
    availableNodes.remove(nextNode);
  }

  /**
   * A faster power function than Math.pow.
   */
  public static double pow(final double x, final double y) {
    final long tmp = Double.doubleToLongBits(x);
    final long tmp2 = (long) (y * (tmp - 4606921280493453312L)) + 4606921280493453312L;
    return Double.longBitsToDouble(tmp2);
  }

  /**
   * Next edges index calculation.
   * 
   * @param probs - probabilities all edges
   * @return - Next edges index
   */
  protected int next(double[] probs) {
    double randomNumber = rand.nextDouble();
    double numberReach;
    if (randomNumber <= 0.5) {
      numberReach = 0;
      for (int i = 0; i < probs.length; i++) {
        numberReach += probs[i];
        if (numberReach > randomNumber) {
          return i;
        }
      }
    } else {
      numberReach = 1;
      for (int i = probs.length - 1; i >= 0; i--) {
        numberReach -= probs[i];
        if (numberReach <= randomNumber) {
          return i;
        }
      }
    }
    return 0;
  }

  public double getQuantumPheromone() {
    return quantumPheromone;
  }

  public double getPathCost(List<Edge> path) {
    double result = 0;
    for (Edge e : path) {
      result += e.getEdgePrice();
    }

    return result;
  }

  protected List<Node> edgeListToNodeList(List<Edge> edges) {
    ArrayList<Node> nodeList = new ArrayList<Node>();

    Edge previous = null;
    for (Edge e : edges) {
      if (previous != null) {
        if (e.getNode1().getID() == previous.getNode1().getID() 
            || e.getNode1().getID() == previous.getNode2().getID()) {
          nodeList.add(e.getNode2());
        } else {
          nodeList.add(e.getNode1());
        }
      } else {
        nodeList.add(e.getNode1());
        nodeList.add(e.getNode2());
      }
      previous = e;
    }
    return nodeList;
  }
}
