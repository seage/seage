/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
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
 * 
 * @author Martin Zaloga
 */
public class AntBrain
{

    // protected Node _startingNode;
    protected double _alpha, _beta;
    protected double _quantumPheromone;
    private Random _rand;
    protected Graph _graph;
    protected HashSet<Node> _availableNodes;
    protected ArrayList<Node> _availableNodeList;

    public AntBrain(Graph graph)
    {
        _graph = graph;
        _rand = new Random(System.currentTimeMillis());
        _availableNodeList = new ArrayList<Node>();
    }

    void setParameters(double alpha, double beta, double quantumPheromone)
    {
        _alpha = alpha;
        _beta = beta;
        _quantumPheromone = quantumPheromone;
    }

    public void reset()
    {
        _availableNodes = null;
        _availableNodeList.clear();
    }

    /**
     * Selection following edge
     * 
     * @param edges - Available edges
     * @param visited - Visited nodes
     * @return - Selected edge
     */
    protected Node selectNextNode(Node firstNode, Node currentNode)
    {
        HashSet<Node> availableNodes = getAvailableNodes(firstNode, currentNode);
        _availableNodeList.clear();

        if (availableNodes == null || availableNodes.size() == 0)
            return null;

        double sum = 0;
        int i = 0;
        double[] probabilities = new double[availableNodes.size()];
        // for each available node calculate probability
        for (Node n : availableNodes)
        {
            double edgePheromone = 0;
            double edgePrice = 0;

            Edge e = currentNode.getEdgeMap().get(n);
            if (e != null)
            {
                edgePheromone = e.getLocalPheromone();
                edgePrice = e.getEdgePrice();
            }
            else
            {
                edgePheromone = _graph.getDefaultPheromone();
                edgePrice = _graph.getNodesDistance(currentNode, n);
            }

            double p = pow(edgePheromone, _alpha) * pow(1 / edgePrice, _beta);
            probabilities[i] = p;
            _availableNodeList.add(n);
            sum += p;
            i++;
        }
        for (i = 0; i < probabilities.length; i++)
        {
            probabilities[i] /= sum;
        }

        Node nextNode = _availableNodeList.get(next(probabilities));
        markSelected(nextNode);

        return nextNode;
    }

    protected HashSet<Node> getAvailableNodes(Node firstNode, Node currentNode)
    {
        if (_availableNodes == null)
        {
            _availableNodes = new HashSet<Node>(_graph.getNodes().values());
            _availableNodes.remove(firstNode);
        }
        return _availableNodes;
    }

    protected void markSelected(Node nextNode)
    {
        _availableNodes.remove(nextNode);
    }

    /**
     * A faster power function than Math.pow
     * @param x
     * @param y
     * @return x^y
     */
    public static double pow(final double x, final double y)
    {
        final long tmp = Double.doubleToLongBits(x);
        final long tmp2 = (long) (y * (tmp - 4606921280493453312L)) + 4606921280493453312L;
        return Double.longBitsToDouble(tmp2);
    }

    /**
     * Next edges index calculation
     * 
     * @param probs - probabilities all edges
     * @return - Next edges index
     */
    protected int next(double[] probs)
    {
        double randomNumber = _rand.nextDouble();
        double numberReach;
        if (randomNumber <= 0.5)
        {
            numberReach = 0;
            for (int i = 0; i < probs.length; i++)
            {
                numberReach += probs[i];
                if (numberReach > randomNumber)
                {
                    return i;
                }
            }
        }
        else
        {
            numberReach = 1;
            for (int i = probs.length - 1; i >= 0; i--)
            {
                numberReach -= probs[i];
                if (numberReach <= randomNumber)
                {
                    return i;
                }
            }
        }
        return 0;
    }

    public double getQuantumPheromone()
    {
        return _quantumPheromone;
    }

    public double getPathCost(List<Edge> path)
    {
        double result = 0;
        for (Edge e : path)
            result += e.getEdgePrice();

        return result;
    }

    protected List<Node> edgeListToNodeList(List<Edge> edges)
    {
        ArrayList<Node> nodeList = new ArrayList<Node>();

        Edge previous = null;
        for (Edge e : edges)
        {
            if (previous != null)
            {
                if (e.getNode1().getID() == previous.getNode1().getID() ||
                        e.getNode1().getID() == previous.getNode2().getID())
                    nodeList.add(e.getNode2());
                else
                    nodeList.add(e.getNode1());
            }
            else
            {
                nodeList.add(e.getNode1());
                nodeList.add(e.getNode2());
            }
            previous = e;
        }
        return nodeList;
    }
}
