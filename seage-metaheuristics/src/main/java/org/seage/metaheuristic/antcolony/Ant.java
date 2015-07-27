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

/**
 *
 * @author Martin Zaloga
 */
public class Ant {

    protected Graph _graph;
    protected double _distanceTravelled;
    protected List<Edge> _edgePath;
    protected List<Integer> _nodeIDsAlongPath;
    
	protected AntBrain _brain;

	public Ant()
    {
		this(new ArrayList<Integer>());
    }	
	
	public Ant(List<Integer> nodeIDs)
    {
		_nodeIDsAlongPath = new ArrayList<Integer>(nodeIDs);
		_edgePath = new ArrayList<Edge>();
    }
	
	void setParameters(Graph graph, AntBrain brain, double alpha, double beta, double quantumPheromone)
	{
		_graph = graph;
		_brain = brain;
		_brain.setParameters(alpha, beta, quantumPheromone);
	}
	
	/**
	 * Do a first exploration if nodeIDs collection is set
	 * @return A path traveled
	 * @throws Exception
	 */
	public List<Edge> doFirstExploration() throws Exception
	{
		_edgePath = new ArrayList<Edge>();
		_distanceTravelled = 0;
		
		for(int i=0;i<_nodeIDsAlongPath.size()-1;i++)
		{
			Node n1 = _graph.getNodes().get(_nodeIDsAlongPath.get(i));
			Node n2 = _graph.getNodes().get(_nodeIDsAlongPath.get(i+1));
			Edge e = n1.getEdgeMap().get(n2);
			if(e == null)			
				e = _graph.createEdge(n1, n2);
			_distanceTravelled += e.getEdgePrice();
			_edgePath.add(e);
		}
		
		leavePheromone();
		
		return _edgePath;
	}
	
    /**
     * Ant exploring the graph
     * @return Ant's path
     * @throws Exception 
     */
    protected List<Edge> explore(Node startingNode) throws Exception
    {
    	_nodeIDsAlongPath.clear();
    	_edgePath.clear();        
        _distanceTravelled = 0; 
        _brain.reset();
        
        _nodeIDsAlongPath.add(startingNode.getID());
        
        Node currentNode = startingNode;        
        Node nextNode = _brain.selectNextNode(startingNode, currentNode);
        
        while (nextNode != null) 
        {
            Edge nextEdge = currentNode.getEdgeMap().get(nextNode);
            if(nextEdge==null)
            {
            	nextEdge = nextNode.getEdgeMap().get(currentNode);
            	if(nextEdge==null)
            		nextEdge = _graph.createEdge(currentNode, nextNode);
            	else
            		nextNode.getEdgeMap().put(currentNode, nextEdge);
            }
            _distanceTravelled +=  nextEdge.getEdgePrice();            
            
            _edgePath.add(nextEdge);
            _nodeIDsAlongPath.add(nextNode.getID());
            
            currentNode = nextNode;
            nextNode = _brain.selectNextNode(startingNode, currentNode);
        }        

        leavePheromone();
        return _edgePath;
    }

    /**
     * Pheromone leaving
     */
    protected void leavePheromone() 
    {
        for (Edge edge : _edgePath) 
        {
            edge.addLocalPheromone(_brain.getQuantumPheromone() / (_distanceTravelled));
        }
    }
    
	public AntBrain getBrain()
	{
		return _brain;
	}
	
    public List<Integer> getNodeIDsAlongPath()
	{
		return _nodeIDsAlongPath;
	}
    
    public double getDistanceTravelled()
    {
    	return _distanceTravelled;
    }
}
