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

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.tsp.City;

/**
 *
 * @author Zagy
 */
public class TspGraph extends Graph {

    private City[] _cities;

	public TspGraph(City[] cities) throws Exception {
        super();
        _cities = cities;
        for (int id = 1; id <= cities.length; id++) {
            _nodes.put(new Integer(id), new Node(id));
        }

        //fillEdgeMap(cities);
    }

    /**
     * Edge length calculating
     * @param start - Starting node
     * @param end - Terminate node
     * @param cities - Readed cities
     * @return - Euclide edge length
     */
    public double calculateEdgeLength(Node start, Node end) {
        double _dX = (_cities[start.getID()-1].X - _cities[end.getID()-1].X);
        double _dY = (_cities[start.getID()-1].Y - _cities[end.getID()-1].Y);
        return Math.sqrt(_dX * _dX + _dY * _dY);
    }

    /**
     * List of graph edges filling
     * @throws Exception 
     */
//    private void fillEdgeMap(City[] cities) throws Exception {
//    	for(int i=0;i<_nodes.size();i++)			// 0 1 2 3: 0-1, 0-2, 0-3, 1-2, 1-3, 2-3
//    	{	
//    		for(int j=i+1;j<_nodes.size();j++)
//    		{    			
//    			Node n1 = _nodes.get(i);
//    			Node n2 = _nodes.get(j);
//    			if (n1.equals(n2)) continue;    				
//    			
//    			Edge edge = new Edge(n1, n2);
//    			edge.setEdgePrice(calculateEdgeLength(n1, n2, cities));
//    			_edges.add(edge);
//    			n1.addEdge(edge);
//    			n2.addEdge(edge);
//    		}	
//    	}
//    }
}
