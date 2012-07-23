/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
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
