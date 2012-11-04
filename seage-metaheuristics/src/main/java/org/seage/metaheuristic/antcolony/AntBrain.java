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

import java.util.*;

/**
 *
 * @author Martin Zaloga
 */
public class AntBrain{

    protected Node _startingNode;
    protected double _alpha, _beta;
    protected double _quantumPheromone;
    private Random _rand;

    public AntBrain() 
    {
        _rand = new Random(System.currentTimeMillis());
    }

    void setParameters(double alpha, double beta, double quantumPheromone)
    {
        _alpha = alpha;
        _beta = beta;
        _quantumPheromone = quantumPheromone;
    }

    /**
     * Finding available edges
     * @param currentPosition - Current position
     * @param visited - Visited nodes
     * @return - Available edges (a full graph)
     */
    protected List<Edge> getAvailableEdges(Node currentPosition, HashSet<Node> visited)
    {
        List<Edge> result = new ArrayList<Edge>();
        for(Edge e : currentPosition.getConnectionMap())
        {
            Node node2 = null;
            if(e.getNode1().equals(currentPosition))
                node2 = e.getNode2();
            else
                node2 = e.getNode1();

//            if(visited.size() == _numNodes)
//                if(node2 == _startingNode)
//                {
//                    result.add(e);
//                    return result;
//                }

            if(!visited.contains(node2))
                result.add(e);
        }

        return result;
    }

    /**
     * Selection following edge
     * @param edges - Available edges
     * @param visited - Visited nodes
     * @return - Selected edge
     */
    protected Edge selectNextEdge(List<Edge> edges, HashSet<Node> visited) {
        double sum = 0;
        double[] probabilities = new double[edges.size()];
        // for each Edges
        for (int i = 0; i < probabilities.length; i++) {
            Edge e = edges.get(i);
            for (Node n : e.getConnections()) {
                if (visited.contains(n)) {
                    continue;
                } else {
                    probabilities[i] = Math.pow(e.getLocalPheromone(), _alpha) * Math.pow(1 / e.getEdgePrice(), _beta);
                    sum += probabilities[i];
                }
            }
        }
        for (int i = 0; i < probabilities.length; i++) {
            probabilities[i] /= sum;
        }
        return edges.get(next(probabilities));
    }

    /**
     * Next edges index calculation
     * @param probs - probabilities all edges
     * @return - Next edges index
     */
    protected int next(double[] probs) {
        double randomNumber = _rand.nextDouble();
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

	public double getQuantumPheromone()
	{
		return _quantumPheromone;
	}
}
