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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.qap.fireflies;

import org.seage.metaheuristic.fireflies.Move;
import org.seage.metaheuristic.fireflies.Solution;

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
    } // end constructor

    @Override
    public void operateOn(Solution soln)
    {
        Integer[] assign = ((QapSolution) soln).assign;
        int pos1 = -1;
        int pos2 = -1;

        // Find positions
        for (int i = 0; i < assign.length && pos1 < 0; i++)
            if (assign[i] == customer)
                pos1 = i;
        pos2 = pos1 + movement;

        // Swap
        int cust2 = assign[pos2];
        assign[pos1] = cust2;
        assign[pos2] = customer;
    } // end operateOn

    /** Identify a move for SimpleTabuList */
    @Override
    public int hashCode()
    {
        return customer;
    } // end hashCode

} // end class MySwapMove
