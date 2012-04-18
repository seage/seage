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
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.tsp.grasp;

import org.seage.metaheuristic.grasp.ISolutionGenerator;
import org.seage.metaheuristic.grasp.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;

/**
 *
 * @author Zagy
 */
public class TSPSolutionGenerator implements ISolutionGenerator {

    /**
     * _cities - Loaded cities
     * _switcher - Parameter setting random or greedy initial solution
     */
    private City[] _cities;
    private String _switcher;

    /**
     * The constructor of object TSPSolutionGenerator
     * @param switcher - Loaded cities
     * @param cities - Parameter setting random or greedy initial solution
     */
    public TSPSolutionGenerator(String switcher, City[] cities) {
        _cities = cities;
        _switcher = switcher;
    }

    /**
     * Function to generate the initial solution
     * @return
     */
    public Solution generateSolution() {

        TspSolution tspSolution = null;

        /*Initialization for the random initial solution*/
        if (_switcher.equals("Random") || _switcher.equals("random")) {
            tspSolution = new TspRandomSolution(_cities);
        }

        /*Initialization for the greedy initial solution*/
        if (_switcher.equals("Greedy") || _switcher.equals("greedy")) {
            tspSolution = new TspGreedySolution(_cities);
        }
        return tspSolution;
    }
}
