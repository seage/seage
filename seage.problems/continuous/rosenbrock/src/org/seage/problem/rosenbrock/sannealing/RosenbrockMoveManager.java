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

        int index = rnd.nextInt( rosSolution.getCoords().length );
        rosSolution.getCoords()[index] = Math.random();

        return (Solution)rosSolution;
    }

}
