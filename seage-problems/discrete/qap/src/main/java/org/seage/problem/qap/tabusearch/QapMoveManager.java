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
package org.seage.problem.qap.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 *
 * @author Karel Durkota
 */

public class QapMoveManager implements MoveManager
{

    @Override
    public Move[] getAllMoves(Solution solution)
    {
        Integer[] assign = ((QapSolution) solution)._assign;
        Move[] buffer = new Move[assign.length * assign.length];
        int nextBufferPos = 0;

        // Generate moves that move each customer
        // forward and back up to five spaces.
        for (int i = 1; i < assign.length; i++)
            for (int j = -assign.length / 5; j <= assign.length / 5; j++)
                if ((i + j >= 1) && (i + j < assign.length) && (j != 0))
                    buffer[nextBufferPos++] = new QapSwapMove(assign[i], j);

        Move[] moves = new Move[nextBufferPos];
        System.arraycopy(buffer, 0, moves, 0, nextBufferPos);

        return moves;
    } // end getAllMoves

} // end class MyMoveManager
