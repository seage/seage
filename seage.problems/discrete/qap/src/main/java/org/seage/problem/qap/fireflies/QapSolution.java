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
package org.seage.problem.qap.fireflies;

import org.seage.metaheuristic.fireflies.*;

/**
 *
 * @author Karel Durkota
 */
public class QapSolution extends SolutionAdapter
{
    protected Integer[] _assign;
    
    public QapSolution(){} // Appease clone()

    public QapSolution(Double[][][] customers)
    {
        // Crudely initialize solution
        _assign = new Integer[ customers.length ];
        for( int i = 0; i < customers.length; i++ )
            _assign[i] = i;
    }   // end constructor
    
    public QapSolution(Integer[] assign){
        _assign = assign;
    }

    public Object clone()
    {
		QapSolution copy = (QapSolution)super.clone();
		copy._assign = (Integer[])this._assign.clone();
        return copy;
    }   // end clone

    public Integer[] getAssign()
    {
            return _assign;
    }

    public void setAssign(Integer[] assign)
    {
            _assign = assign;
    }
    
    public String toString()
    {
        StringBuffer s = new StringBuffer();

        //s.append( new Double(getObjectiveValue()[0]).toString().substring(0,new Double(getObjectiveValue()[0]).toString().length()) +"\t" + hashCode());
        s.append("[");
        for(int i=_assign.length-1;i>=0;i--){
            s.append((_assign[i]+1));
            s.append(",");
        }
        s.append("] - ");
        s.append(getObjectiveValue()[0]);
		//s.append( "Sequence: [ " );
        
		//for( int i = 0; i < tour.length - 1; i++ )
		  //  s.append( tour[i] ).append( ", " );
        
		//s.append( tour[ tour.length - 1 ] );
		//s.append( " ]" );
        
        return s.toString();
    }   // end toString

    @Override
    public boolean equals(Solution in){
        QapSolution q = (QapSolution)in;
        for(int i=0;i<_assign.length;i++){
            if(_assign[i]!=q.getAssign()[i])
                return false;
        }
        return true;
    }

    
}   // end class MySolution
