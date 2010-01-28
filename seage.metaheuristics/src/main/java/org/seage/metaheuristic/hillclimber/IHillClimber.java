/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.metaheuristic.hillclimber;

/**
 *
 * @author Martin Zaloga
 */
public interface IHillClimber {

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
    void startSearching(Solution solution, String calsic);

    /**
     *
     * @param solution
     * @param classic
     * @param numRestarts
     */
    void startRestartedSearching(String classic, String switcher, int numRestarts);

    /**
     * Defining the shape functions for getting the best solution
     * @return - The best solution
     */
    Solution getBestSolution();
}
