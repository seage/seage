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
    protected List<Integer> _nodeIDs;
    
	protected AntBrain _brain;

	public Ant(AntBrain brain)
    {
		_brain = brain;		
    }	
	
	public Ant(AntBrain brain, List<Integer> nodeIDs)
    {
		this(brain);
		_nodeIDs = nodeIDs;
    }
	
	public List<Edge> doFirstExploration(Graph graph) throws Exception
	{
		_path = _brain.getEdgesToNodes(_nodeIDs, graph);
		_distanceTravelled = 0;
		for (Edge edge : _path)
		{
			_distanceTravelled += edge.getEdgePrice();
			edge.addLocalPheromone(_brain.getQuantumPheromone() / (_distanceTravelled));
		}
		
		return _path;
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

        _nodeIDs.clear();
        List<Edge> edges = _brain.getAvailableEdges(_currentPosition, _visited);
        
        while (edges != null && edges.size() > 0) 
        {
            Edge nextEdge = _brain.selectNextEdge(edges, _visited);
            Node nextNode = nextEdge.getNode1().equals(_currentPosition) ? nextEdge.getNode2() : nextEdge.getNode1();
            
            _distanceTravelled += nextEdge.getEdgePrice();
            _path.add(nextEdge);
            _visited.add(nextNode);
            _currentPosition = nextNode;
            _nodeIDs.add(nextNode.getID());
            
            edges = _brain.getAvailableEdges(_currentPosition, _visited);
        }        

        leavePheromone();
        return _path;
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
	
    public List<Integer> getNodeIDs()
	{
		return _nodeIDs;
	}
    
    public double getDistanceTravelled()
    {
    	return _distanceTravelled;
    }
}
