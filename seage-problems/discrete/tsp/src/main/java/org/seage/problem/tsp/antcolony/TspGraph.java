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

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 *
 * @author Zagy
 */
public class TspGraph extends Graph {

    public TspGraph(City[] cities) {
        super();
        for (int id = 0; id < cities.length; id++) {
            _nodeList.add(new Node(id));
        }

        fillEdgeMap(cities);
    }

    /**
     * Edge length calculating
     * @param start - Starting node
     * @param end - Terminate node
     * @param cities - Readed cities
     * @return - Euclide edge length
     */
    private double calculateEdgeLength(Node start, Node end, City[] cities) {
        double _dX = (cities[start.getId()].X - cities[end.getId()].X);
        double _dY = (cities[start.getId()].Y - cities[end.getId()].Y);
        return Math.sqrt(_dX * _dX + _dY * _dY);
    }

    /**
     * List of graph edges filling
     */
    private void fillEdgeMap(City[] cities) {
    	for(int i=0;i<_nodeList.size();i++)			// 0 1 2 3: 0-1, 0-2, 0-3, 1-2, 1-3, 2-3
    	{	
    		for(int j=i+1;j<_nodeList.size();j++)
    		{    			
    			Node n1 = _nodeList.get(i);
    			Node n2 = _nodeList.get(j);
    			if (n1.equals(n2)) continue;    				
    			
    			Edge edge = new Edge(n1, n2);
    			edge.setEdgePrice(calculateEdgeLength(n1, n2, cities));
    			_edgeList.add(edge);
    			n1.addConnection(edge);
    			n2.addConnection(edge);
    		}	
    	}
    }
}
