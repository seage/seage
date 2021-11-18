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

  public AntBrain(Graph graph) {
    this.graph = graph;
    rand = new Random(System.currentTimeMillis());
  }

  void setParameters(double alpha, double beta, double quantumPheromone) {
    this.alpha = alpha;
    this.beta = beta;
    this.quantumPheromone = quantumPheromone;
  }

  protected Edge selectNextStep(List<Node> nodePath) throws Exception {
    Node currentNode = nodePath.get(nodePath.size()-1);
    HashSet<Node> nextAvailableNodes = getAvailableNodes(nodePath);

    if (nextAvailableNodes.isEmpty())
      return null;

    double probSum = 0; 
    int i = 0;
    double[] probabilities = new double[nextAvailableNodes.size()];
    Edge[] candidateEdges = new Edge[nextAvailableNodes.size()];
    
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
        edgePrice = getNodeDistance(nodePath, n);
        e = new Edge(currentNode, n, edgePrice);
        e.setEdgePrice(edgePrice);
        e.addLocalPheromone(graph.getDefaultPheromone());
      }

      double p = pow(edgePheromone, alpha) * pow(1 / edgePrice, beta);
      probabilities[i] = p;
      candidateEdges[i] = e;
      probSum += p;
    }
    
    return candidateEdges[next(probabilities, probSum)];
  }

  protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
    var result = new HashSet<Node>(graph.getNodes().values());
    result.removeAll(nodePath);
    return result;
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
  protected int next(double[] probabilities, double sum) {
    sum = sum != 0 ? sum : 1;    
    for (int i = 0; i < probabilities.length; i++) {
      probabilities[i] /= sum;
    }

    double randomNumber = rand.nextDouble();
    double numberReach;
    if (randomNumber <= 0.5) {
      numberReach = 0;
      for (int i = 0; i < probabilities.length; i++) {
        numberReach += probabilities[i];
        if (numberReach > randomNumber) {
          return i;
        }
      }
    } else {
      numberReach = 1;
      for (int i = probabilities.length - 1; i >= 0; i--) {
        numberReach -= probabilities[i];
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

  public double getNodeDistance(List<Node> nodePath, Node node) {
     return 1.0;
  };
}
