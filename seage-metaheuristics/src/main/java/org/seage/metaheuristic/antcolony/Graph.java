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
public class Graph {

    protected ArrayList<Node> _nodeList;
    protected ArrayList<Edge> _edgeList;
    protected double _evaporCoeff = 0.95;

	public Graph() {
        _nodeList = new ArrayList<Node>();
        _edgeList = new ArrayList<Edge>();
    }
	
    
    
    /**
     * List of nodes of graph
     * @return - List of nodes
     */
    public ArrayList<Node> getNodeList() {
        return _nodeList;
    }

    /**
     * List of edges of graph
     * @return - List of edges
     */
    public ArrayList<Edge> getEdgeList() {
        return _edgeList;
    }

    /**
     * Evaporating from each edges of graph
     */
    public void evaporate() {
        for (Edge e : getEdgeList()) {
            e.evaporateFromEdge(_evaporCoeff);
        }
    }
    
    public void setEvaporCoeff(double evaporCoeff)
	{
		_evaporCoeff = evaporCoeff;
	}
    /**
     * Default pheromone setting for all edges
     * @param defaultPheromone - Default pheromone
     */
    public void setDefaultPheromone(double defaultPheromone) {
        for (Edge e : getEdgeList()) {
            e.setDefaultPheromone(defaultPheromone);
        }
    }

}
