/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.tabusearch;

import org.seage.metaheuristic.tabusearch.*;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author Richard Malek
 */
public class TspSolution extends SolutionAdapter 
{
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
