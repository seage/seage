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
 *     Jan Zmatlik
 *     - Initial implementation
 */

package org.seage.problem.rosenbrock.sannealing;

import java.util.Random;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class RosenbrockMoveManager implements IMoveManager
{
    Random rnd = new Random();

    @Deprecated
    public Solution getModifiedSolutionOld(Solution solution)
    {
        RosenbrockSolution rosSolution = ((RosenbrockSolution)solution).clone();

        // X(i + 1) = Xi + C*u
        // X is a solution
        // u is a vector of random numbers
        // C is a identity matrix
        int length = rosSolution.getCoords().length;
        for(int i = 0; i < length; i++)
        {
            rosSolution.getCoords()[i] = rosSolution.getCoords()[i] + randomNumberOneToMinusOne();
        }

        return (Solution)rosSolution;
    }

    private double randomNumberOneToMinusOne()
    {
        double random = Math.random();
        int rand = rnd.nextInt(2);

        return rand == 0 ? random : -random;
    }

    public Solution getModifiedSolution(Solution solution)
    {
        RosenbrockSolution rosSolution = ((RosenbrockSolution)solution).clone();

        int index = rnd.nextInt( rosSolution.getCoords().length );
        rosSolution.getCoords()[index] += randomNumberOneToMinusOne();

        return (Solution)rosSolution;
    }

    @Deprecated
    public Solution getModifiedSolutionOldOld(Solution solution)
    {
        RosenbrockSolution rosSolution = ((RosenbrockSolution)solution).clone();

        int index = rnd.nextInt( rosSolution.getCoords().length );
        rosSolution.getCoords()[index] = Math.random();

        return (Solution)rosSolution;
    }
}
