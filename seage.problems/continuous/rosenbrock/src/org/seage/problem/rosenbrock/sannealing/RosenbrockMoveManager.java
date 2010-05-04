/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
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

    public Solution getModifiedSolution(Solution solution)
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

    @Deprecated
    public Solution getModifiedSolutionOld(Solution solution)
    {
        RosenbrockSolution rosSolution = ((RosenbrockSolution)solution).clone();

        int index = rnd.nextInt( rosSolution.getCoords().length );
        rosSolution.getCoords()[index] = Math.random();

        return (Solution)rosSolution;
    }
}
