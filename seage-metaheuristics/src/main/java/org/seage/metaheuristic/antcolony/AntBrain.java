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
	
	public AntBrain(Graph graph)
	{	
		_graph = graph;
		_rand = new Random(System.currentTimeMillis());
	}

	void setParameters(double alpha, double beta, double quantumPheromone)
	{
		_alpha = alpha;
		_beta = beta;
		_quantumPheromone = quantumPheromone;
	}

	/**
	 * Selection following edge
	 * 
	 * @param edges - Available edges
	 * @param visited - Visited nodes
	 * @return - Selected edge
	 */
	protected Node selectNextNode(Node currentNode, HashSet<Node> visited)
	{		
		List<Node> nodes = _graph.getAvailableNodes(currentNode, visited);
		
		if(nodes == null || nodes.size()==0)
			return null;
		
		double sum = 0;
		double[] probabilities = new double[nodes.size()];
		// for each available node calculate probability
		for (int i = 0; i < nodes.size(); i++)
		{
			double edgePheromone = 0;
			double edgePrice = 0;
			
			Node n = nodes.get(i);
			Edge e = currentNode.getEdgeMap().get(n);
			if(e != null)
			{
				edgePheromone = e.getLocalPheromone();
				edgePrice = e.getEdgePrice();
			}
			else
			{
				edgePheromone = 0.001;
				edgePrice = _graph.getNodesDistance(currentNode, n);
			}

            double p = pow(edgePheromone, _alpha) * pow(1/edgePrice, _beta);
            probabilities[i] = p;
            sum += p;
		}
		for (int i = 0; i < probabilities.length; i++)
		{
			probabilities[i] /= sum;
		}
		return nodes.get(next(probabilities));
	}
        /**
         * A faster power function than Math.pow
         * @param x
         * @param y
         * @return x^y
         */
        public static double pow(final double x, final double y) {
            final long tmp = Double.doubleToLongBits(x);
            final long tmp2 = (long)(y * (tmp - 4606921280493453312L)) + 4606921280493453312L;
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
}
