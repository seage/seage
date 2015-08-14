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
package org.seage.metaheuristic.grasp;

/**
 *
 * @author Martin Zaloga
 */
public interface IHillClimber
{

    /**
     * Defining the shape of the methods for setting the number of iterations
     * @param count - Number of iterations
     */
    void setIterationCount(int count);

    /**
     * Defining the shape of the methods for starting the algorithm
     * @param solution - Initial solution
     * @param calsic - Determine the version of the algorithm
     */
    void startSearching(Solution solution) throws Exception;

    /**
     * Method for running the restarted search
     * @param classic - Parameter that determines whether the solution can deteriorate
     * @param numRestarts - Number of restarts algorithm
     */
    void startRestartedSearching(int numRestarts) throws Exception;

    /**
     * Defining the shape functions for getting the best solution
     * @return - The best solution
     */
    Solution getBestSolution();
}
