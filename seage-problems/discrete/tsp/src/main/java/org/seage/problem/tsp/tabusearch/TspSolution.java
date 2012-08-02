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
import org.seage.problem.tsp.City;

/**
 *
 * @author Richard Malek
 */
public class TspSolution extends SolutionAdapter 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3688124719557679451L;
	protected int[] _tour;
    
    public TspSolution(){} // Appease clone()

    public TspSolution(City[] customers)
    {
        // Crudely initialize solution
        _tour = new int[ customers.length ];
        for( int i = 0; i < customers.length; i++ )
            _tour[i] = i;
    }   // end constructor
    
    
    public Object clone()
    {
		TspSolution copy = (TspSolution)super.clone();
		copy._tour = (int[])this._tour.clone();
        return copy;
    }   // end clone

    public int[] getTour()
    {
            return _tour;
    }

    public void setTour(int[] tour)
    {
            _tour = tour;
    }
    
    public String toString()
    {
        StringBuffer s = new StringBuffer();

        s.append( new Double(getObjectiveValue()[0]).toString().substring(0,10) +"\t" + hashCode());
		//s.append( "Sequence: [ " );
        
		//for( int i = 0; i < tour.length - 1; i++ )
		//    s.append( tour[i] ).append( ", " );
        
		//s.append( tour[ tour.length - 1 ] );
		//s.append( " ]" );
        
        return s.toString();
    }   // end toString
    
}   // end class MySolution
