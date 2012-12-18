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
public abstract class AntBrain
{

	// protected Node _startingNode;
	protected double _alpha, _beta;
	protected double _quantumPheromone;
	private Random _rand;
	protected Graph _graph;
	
	public abstract double getNodesDistance(Node n1, Node n2);

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
	 * Finding available edges
	 * 
	 * @param currentNode - Current position
	 * @param visited - Visited nodes
	 * @return - Available edges (a full graph)
	 */
	protected ArrayList<Node> getAvailableNodes(Node currentNode, HashSet<Node> visited)
	{
		ArrayList<Node> result = new ArrayList<Node>();
		
		for(Node n : _graph.getNodes().values())
		{
			if (!visited.contains(n) && !n.equals(currentNode))
				result.add(n);
		}
		
//		for (Edge e : currentNode.getEdges())
//		{
//			Node node2 = null;
//			if (e.getNode1().equals(currentNode))
//				node2 = e.getNode2();
//			else
//				node2 = e.getNode1();
//
//			if (!visited.contains(node2))
//				result.add(node2);
//		}

		return result;
	}

	/**
	 * Selection following edge
	 * 
	 * @param edges - Available edges
	 * @param visited - Visited nodes
	 * @return - Selected edge
	 */
	protected Node selectNextNode(Node currentNode, ArrayList<Node> nodes, HashSet<Node> visited)
	{
		double sum = 0;
		double[] probabilities = new double[nodes.size()];
		// for each Edges
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
				edgePrice = getNodesDistance(currentNode, n);
			}
			
			if (!(visited.contains(n)))
			{
                            double p = pow(edgePheromone, _alpha) * pow(1/edgePrice, _beta);
                            probabilities[i] = p;
                            sum += p;
			}

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

	public List<Edge> getEdgesToNodes(List<Integer> _nodeIDs, Graph graph) throws Exception
	{
		List<Edge> edges = new ArrayList<Edge>();
		
		for(int i=0;i<_nodeIDs.size()-1;i++)
		{
			Node n1 = graph.getNodes().get(_nodeIDs.get(i));
			Node n2 = graph.getNodes().get(_nodeIDs.get(i+1));
			Edge e = n1.getEdgeMap().get(n2);
			if(e == null)
				throw new Exception("There is no edge for node IDs: " + n1.getID() + ", " + n2.getID());
			
			edges.add(e);
		}
		
		return edges;
	}
}
