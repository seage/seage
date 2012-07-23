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
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.tsp.grasp;

import java.util.Random;
import org.seage.metaheuristic.grasp.Solution;
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