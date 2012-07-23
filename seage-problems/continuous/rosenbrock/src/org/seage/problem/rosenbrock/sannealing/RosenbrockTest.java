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
        int dimension = 30;

        RosenbrockSolution rosSolution = new RosenbrockSolution(dimension);

        for(int i = 0; i < dimension; i++)
        {
            rosSolution.getCoords()[i] = Math.random();
        }

        SimulatedAnnealing sa = new SimulatedAnnealing(new RosenbrockObjectiveFunction(), new RosenbrockMoveManager());
        sa.setAnnealingCoefficient(0.99);
        sa.setMaximalIterationCount(10000);
        sa.setMaximalSuccessIterationCount(200);
        sa.setMaximalTemperature(200);
        sa.setMinimalTemperature(0.01);

        sa.startSearching(rosSolution);

        System.out.println(">BEST " + sa.getBestSolution().getObjectiveValue());
        System.out.println(">COORDS ");

        for(int i = 0; i < dimension; i++)
            System.out.print(" " + ((RosenbrockSolution)sa.getBestSolution()).getCoords()[i]);

        System.out.println("");
    }
}
