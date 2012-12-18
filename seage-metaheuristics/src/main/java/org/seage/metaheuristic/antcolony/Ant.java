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
import java.util.Vector;

/**
 *
 * @author Martin Zaloga
 */
public class Ant {

    protected Graph _graph;
    protected Node _startPosition;
    protected Node _currentNode;
    protected double _distanceTravelled;
    protected HashSet<Node> _visited;
    protected List<Edge> _path;
    protected List<Integer> _nodeIDs;
    
	protected AntBrain _brain;

	public Ant()
    {
		this(new ArrayList<Integer>());
    }	
	
	public Ant(List<Integer> nodeIDs)
    {
		_nodeIDs = nodeIDs;
    }
	
	void setParameters(Graph graph, AntBrain brain, double alpha, double beta, double quantumPheromone)
	{
		_graph = graph;
		_brain = brain;
		_brain.setParameters(alpha, beta, quantumPheromone);
	}
	
	public List<Edge> doFirstExploration(Graph graph) throws Exception
	{
		_path = _brain.getEdgesToNodes(_nodeIDs, graph);
		_distanceTravelled = 0;
		for (Edge edge : _path)
		{
			_distanceTravelled += edge.getEdgePrice();			
		}
		for (Edge edge : _path)
		{
			edge.addLocalPheromone(_brain.getQuantumPheromone() / (_distanceTravelled));
		}
		
		return _path;
	}
	
    /**
     * Ant passage through the graph
     * @return - ants path
     * @throws Exception 
     */
    protected List<Edge> explore(Node startingNode) throws Exception
    {
        _visited = new HashSet<Node>();
        _path = new ArrayList<Edge>();
        _distanceTravelled = 0;
        _currentNode = startingNode;
        _visited.add(startingNode);

        _nodeIDs.clear();
        ArrayList<Node> nodes = _brain.getAvailableNodes(_currentNode, _visited);
        
        while (nodes != null && nodes.size() > 0) 
        {
            Node nextNode = _brain.selectNextNode(_currentNode, nodes, _visited);
            //Node nextNode = nextEdge.getNode1().equals(_currentPosition) ? nextEdge.getNode2() : nextEdge.getNode1();
            Edge nextEdge = _currentNode.getEdgeMap().get(nextNode);
            if(nextEdge==null)
            {
            	nextEdge = _graph.createEdge(_currentNode, nextNode);
            }     
            
            _distanceTravelled +=  nextEdge.getEdgePrice();            
            
            _path.add(nextEdge);
            _visited.add(nextNode);
            _currentNode = nextNode;
            _nodeIDs.add(nextNode.getID());
            
            nodes = _brain.getAvailableNodes(_currentNode, _visited);
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
