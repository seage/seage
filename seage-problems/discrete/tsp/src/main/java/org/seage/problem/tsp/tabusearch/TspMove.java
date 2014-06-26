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

import org.seage.metaheuristic.tabusearch.*;

/**
 *
 * @author Richard Malek
 */
public class TspMove implements Move 
{
    public int ix1;
    public int ix2;

	public TspMove(int ix1, int ix2)
    {   
        this.ix1 = ix1;
        this.ix2 = ix2;
    }   // end constructor
    
    
    public void operateOn( Solution soln )
    {
    	TspSolution tspSoln = (TspSolution)soln; 
    	Integer[] tour = tspSoln._tour;
        
        // Swap
        int tmpIx = tour[ix1];
        tour[ix1] = tour[ix2];
        tour[ix2] = tmpIx;        
    }   // end operateOn
    
    
    /** Identify a move for SimpleTabuList */
    public int hashCode()
    {   
        //return _hashCode;
        return ix1*1000000+ix2;
    }   // end hashCode
    
    public String toString()
    {
    	return "{"+ix1 + ", "+ix2+"}";
    }
    
}   // end class MySwapMove
