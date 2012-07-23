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
package org.seage.problem.qap.tabusearch;

import org.seage.metaheuristic.tabusearch.*;

/**
 *
 * @author Karel Durkota
 */
public class QapSwapMove implements Move
{
    public int customer;
    public int movement;


	public QapSwapMove(int customer, int movement)
    {   
        this.customer = customer;
        this.movement = movement;
    }   // end constructor
    
    
    public void operateOn( Solution soln )
    {
        Integer[] assign = ((QapSolution)soln)._assign;
        int   pos1 = -1;
        int   pos2 = -1;
        
        // Find positions
        for( int i = 0; i < assign.length && pos1 < 0; i++ )
            if( assign[i] == customer )
                pos1 = i;
        pos2 = pos1 + movement;
        
        // Swap
        int cust2 = assign[pos2];
        assign[pos1] = cust2;
        assign[pos2] = customer;
    }   // end operateOn
    
    
    /** Identify a move for SimpleTabuList */
    public int hashCode()
    {   
        return customer;
    }   // end hashCode
    
}   // end class MySwapMove
