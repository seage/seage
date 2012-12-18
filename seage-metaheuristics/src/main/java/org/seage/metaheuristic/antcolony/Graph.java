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
public abstract class Graph {

    protected HashMap<Integer, Node> _nodes;
    protected ArrayList<Edge> _edges;
    protected double _evaporCoeff = 0.95;

	public Graph() {
        _nodes = new HashMap<Integer, Node>();
        _edges = new ArrayList<Edge>();
    }
	
	public abstract double getNodesDistance(Node n1, Node n2);
    
    /**
     * List of nodes of graph
     * @return - List of nodes
     */
    public HashMap<Integer, Node> getNodes() {
        return _nodes;
    }

    /**
     * List of edges of graph
     * @return - List of edges
     */
    public ArrayList<Edge> getEdges() {
        return _edges;
    }

    /**
     * Evaporating from each edges of graph
     */
    public void evaporate() {
        for (Edge e : getEdges()) {
            e.evaporateFromEdge(_evaporCoeff);
        }
    }

    /**
     * Default pheromone setting for all edges
     * @param defaultPheromone - Default pheromone
     */
    public void setDefaultPheromone(double defaultPheromone) {
        for (Edge e : getEdges()) {
            e.addLocalPheromone(defaultPheromone);
        }
    }
    
	public void setEvaporCoeff(double evaporCoeff)
	{
		_evaporCoeff = evaporCoeff;
	}



	public Edge createEdge(Node n1, Node n2) throws Exception
	{
		Edge newEdge = new Edge(n1, n2);
		newEdge.setEdgePrice(getNodesDistance(n1, n2));
		_edges.add(newEdge);	
		
		return newEdge;
	}

}
