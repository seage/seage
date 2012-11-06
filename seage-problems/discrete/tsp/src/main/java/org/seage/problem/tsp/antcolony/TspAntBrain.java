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
import org.seage.metaheuristic.antcolony.Node;

/**
 * 
 * @author Zagy
 */
public class TspAntBrain extends AntBrain
{
	private Node _startingNode;
	private int _numNodes;

	public TspAntBrain(Node startingNode, int numNodes)
	{
		super();
		_startingNode = startingNode;
		_numNodes = numNodes;
	}
	
	@Override
	protected List<Edge> getAvailableEdges(Node currentPosition, HashSet<Node> visited)
	{
		List<Edge> result = new ArrayList<Edge>();
		for (Edge e : currentPosition.getConnectionMap())
		{
			Node node2 = null;
			if (e.getNode1().equals(currentPosition))
				node2 = e.getNode2();
			else
				node2 = e.getNode1();

			if (visited.size() == _numNodes)
				if (node2 == _startingNode)
				{
					result.add(e);
					return result;
				}

			if (!visited.contains(node2))
				result.add(e);
		}

		return result;
	}
}
