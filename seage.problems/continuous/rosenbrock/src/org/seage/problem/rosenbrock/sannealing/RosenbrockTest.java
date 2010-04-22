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

import org.seage.metaheuristic.sannealing.SimulatedAnnealing;

/**
 *
 * @author Jan Zmatlik
 */
public class RosenbrockTest {

    public static void main(String[] args)
    {
        int dimension = 2;

        RosenbrockSolution rosSolution = new RosenbrockSolution(dimension);

        for(int i = 0; i < dimension; i++)
        {
            rosSolution.getCoords()[i] = Math.random();
        }
        
        SimulatedAnnealing sa = new SimulatedAnnealing(new RosenbrockObjectiveFunction() , new RosenbrockMoveManager());
        sa.setAnnealingCoefficient(0.99);
        sa.setMaximalIterationCount(2000);
        sa.setMaximalSuccessIterationCount(200);
        sa.setMaximalTemperature(200);
        sa.setMinimalTemperature(0.01);

        sa.startSearching(rosSolution);

        System.out.println(">BEST " + rosSolution.getObjectiveValue());
        System.out.println(">COORDS ");
        for(int i = 0; i < dimension; i++)
            System.out.print(" " + rosSolution.getCoords()[i]);
    }
}
