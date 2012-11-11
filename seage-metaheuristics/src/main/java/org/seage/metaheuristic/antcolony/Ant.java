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
import java.util.Vector;

/**
 *
 * @author Martin Zaloga
 */
public class Ant {

    
    protected Node _startPosition;
    protected Node _currentPosition;
    protected double _distanceTravelled;
    protected HashSet<Node> _visited;
    protected List<Edge> _path;
    protected Integer[] _nodeIDs;
    
	protected AntBrain _brain;

	public Ant(AntBrain brain)
    {
		_brain = brain;		
    }	
	
	public Ant(AntBrain brain, Integer[] nodeIDs)
    {
		this(brain);
		_nodeIDs = nodeIDs;
    }
	
    /**
     * Ant passage through the graph
     * @return - ants path
     */
    protected List<Edge> explore(Node startingNode)
    {
        _visited = new HashSet<Node>();
        _path = new Vector<Edge>();     
        _distanceTravelled = 0;
        _currentPosition = startingNode;
        _visited.add(startingNode);

        List<Edge> edges = _brain.getAvailableEdges(_currentPosition, _visited);
        while (edges != null && edges.size() > 0) {
            Edge nextEdge = _brain.selectNextEdge(edges, _visited);
            updatePosition(nextEdge);
            edges = _brain.getAvailableEdges(_currentPosition, _visited);
        }        

        //_distanceTravelled = _brain.pathCost(_path);
        leavePheromone();
        return _path; // Report
    }

    /**
     * Update ants position
     * @param selectedEdge - Actual selected edge
     */
    protected void updatePosition(Edge arcChoice) {
        Node choiceNode;
        if (arcChoice.getNode1().equals(_currentPosition)) {
            choiceNode = (arcChoice.getNode2());
        } else {
            choiceNode = (arcChoice.getNode1());
        }
        _distanceTravelled += arcChoice.getEdgePrice();
        _path.add(arcChoice);
        _visited.add(choiceNode);
        _currentPosition = choiceNode;
    }

    /**
     * Pheromone leaving
     */
    protected void leavePheromone() {
        for (Edge edge : _path) {
            edge.addLocalPheromone(_brain.getQuantumPheromone() / (_distanceTravelled));
        }
    }
	public AntBrain getBrain()
	{
		return _brain;
	}
	
    public Integer[] getNodeIDs()
	{
		return _nodeIDs;
	}
}
