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
package org.seage.problem.tsp.tabusearch;

import org.seage.problem.tsp.City;

/**
 *
 * @author Richard Malek
 */

public class TspGreedyStartSolution extends TspSolution 
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5260083820233603440L;


	public TspGreedyStartSolution(){} // Appease clone()

    public TspGreedyStartSolution(City[] customers)
    {
        // Greedy neighbor initialize
        int[] avail = new int[ customers.length ];
        _tour = new int[ customers.length ];
        for( int i = 0; i < avail.length; i++ )
            avail[i] = i;
        for( int i = 1; i < _tour.length; i++ )
        {
            int closest = -1;
            double dist = Double.MAX_VALUE;
            for( int j = 1; j < avail.length; j++ )
                if( (norm( customers, _tour[i-1], j ) < dist) && (avail[j] >= 0) )
                {
                    dist = norm( customers, _tour[i-1], j );
                    closest = j;
                }   // end if: new nearest neighbor
            _tour[i] = closest;
            avail[closest] = -1;
        }   // end for


    }   // end constructor


    private double norm( City[]matr, int a, int b )
    {
        double xDiff = matr[b].X - matr[a].X;
        double yDiff = matr[b].Y - matr[a].Y;
        return Math.sqrt( xDiff*xDiff + yDiff*yDiff );
    }   // end norm

    
}   // end class TspGreedyStartSolution
