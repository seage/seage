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
package org.seage.problem.qap.tabusearch;

import org.seage.metaheuristic.tabusearch.*;

/**
 *
 * @author Karel Durkota
 */
public class QapSolution extends SolutionAdapter
{
    protected int[] _assign;
    
    public QapSolution(){} // Appease clone()

    public QapSolution(Double[][] customers)
    {
        // Crudely initialize solution
        _assign = new int[ customers.length ];
        for( int i = 0; i < customers.length; i++ )
            _assign[i] = i;
    }   // end constructor
    
    
    public Object clone()
    {
		QapSolution copy = (QapSolution)super.clone();
		copy._assign = (int[])this._assign.clone();
        return copy;
    }   // end clone

    public int[] getAssign()
    {
            return _assign;
    }

    public void setAssign(int[] assign)
    {
            _assign = assign;
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
