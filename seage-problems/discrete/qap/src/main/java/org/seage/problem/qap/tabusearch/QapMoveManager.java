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

public class QapMoveManager implements MoveManager
{
    
    @Override
    public Move[] getAllMoves( Solution solution )
    {
        Integer[] assign = ((QapSolution)solution)._assign;
        Move[] buffer = new Move[ assign.length*assign.length ];
        int nextBufferPos = 0;
        
        // Generate moves that move each customer
        // forward and back up to five spaces.
        for (int i = 1; i < assign.length; i++)
            for (int j = -assign.length/5; j <= assign.length/5; j++)
                if ((i + j >= 1) && (i + j < assign.length) && (j != 0))
                    buffer[nextBufferPos++] = new QapSwapMove(assign[i], j);

        Move[] moves = new Move[ nextBufferPos];
        System.arraycopy( buffer, 0, moves, 0, nextBufferPos );
        
        return moves;
    }   // end getAllMoves
    
}   // end class MyMoveManager
