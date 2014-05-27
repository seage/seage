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

import java.util.ArrayList;

import org.seage.metaheuristic.tabusearch.*;

/**
 *
 * @author Richard Malek
 */

public class TspMoveManager implements MoveManager
{
    
    @Override
    public Move[] getAllMoves( Solution solution )
    {
    	Integer[] tour = ((TspSolution)solution)._tour;
        ArrayList<Move> buffer = new ArrayList<Move>();
        int span = Math.max(5, tour.length/5);        
        
        // Generate moves that move each customer
        // forward and back up to five spaces.
        for (int i = 0; i < tour.length; i++)
            for (int j = -span; j <= span; j++)
                if ((i + j > i) && (i + j < tour.length) /*&& (i != j)*/)
                    buffer.add(new TspSwapMove(i, i + j));
                
        return buffer.toArray(new Move[]{});
    }   // end getAllMoves
    
}   // end class MyMoveManager
