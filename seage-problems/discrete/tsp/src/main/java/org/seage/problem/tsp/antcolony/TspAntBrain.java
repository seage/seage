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
 */
package org.seage.problem.tsp.antcolony;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 * 
 * @author Zagy
 */
public class TspAntBrain extends AntBrain
{
	private Node _startingNode;	
	private TspGraph _graph;

	public TspAntBrain(TspGraph graph, Node startingNode)
	{
		super(graph);
		_startingNode = startingNode;
		_graph = graph;
	}
	
	@Override
	protected ArrayList<Node> getAvailableNodes(Node currentNode, HashSet<Node> visited)
	{
		ArrayList<Node> result = super.getAvailableNodes(currentNode, visited);
		if(visited.size()==_graph.getNodes().values().size() && !currentNode.equals(_startingNode))
			result.add(_startingNode);
		return result;
	}

	@Override
	public List<Edge> getEdgesToNodes(List<Integer> _nodeIDs, Graph graph) throws Exception
	{
		List<Edge> result = super.getEdgesToNodes(_nodeIDs, graph);
		Node n1 = graph.getNodes().get(_nodeIDs.get(0));
		Node n2 = graph.getNodes().get(_nodeIDs.get(_nodeIDs.size()-1));
		
		Edge e = n1.getEdgeMap().get(n2);
		if(e == null)
			e = graph.createEdge(n1, n2);
		result.add(e);

		return result;
	}	
}
