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

/**
 *
 * @author Karel Durkota
 */

public class QapGreedyStartSolution extends QapSolution
{
    
    public QapGreedyStartSolution(){} // Appease clone()

    public QapGreedyStartSolution(Double[][] customers)
    {

        _assign = new int[customers.length];

        // Greedy cheapset initialize
        int[] avail = new int[ customers.length ];

        for( int i = 0; i < avail.length; i++ )
        {
            _assign[i] = -1;
            avail[i] = i;
        }

        for(int i=0;i<_assign.length;i++){
            int location = -1;
            double price = Double.MAX_VALUE;
            for( int j=0;j<avail.length;j++)
                if(customers[i][j] < price && avail[j] >= 0){
                    price = customers[i][j];
                    location = j;
                }
            _assign[i]=location;
            avail[location] = -1;
        }
    }   // end constructor

}   // end class TspGreedyStartSolution
