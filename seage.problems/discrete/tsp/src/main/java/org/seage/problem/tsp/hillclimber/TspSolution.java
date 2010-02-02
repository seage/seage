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
package org.seage.problem.tsp.hillclimber;

import java.util.Random;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author Martin Zaloga
 */
public class TspSolution extends Solution {

    /**
     * _tour - Contains the tour of the solution
     * _rnd - Variable for the purposes of generating the random numbers
     */
    protected Integer[] _tour;
    protected Random _rnd;

    /**
     * Constructor the solution with using the random algorithm for initial solution
     */
    public TspSolution() {
        _rnd = new Random();
    }

    /**
     * Function for getting tour
     * @return - actual tour
     */
    public Integer[] getTour() {
        return _tour;
    }

    /**
     * Method for setting tour
     * @param tour - Setting tour
     */
    public void setTour(Integer[] tour) {
        _tour = tour;
    }
}