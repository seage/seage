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

import org.seage.metaheuristic.hillclimber.ISolutionGenerator;
import org.seage.metaheuristic.hillclimber.Solution;
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
