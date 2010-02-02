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

import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.City;
import org.seage.metaheuristic.hillclimber.HillClimber;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.Visualizer;

/**
 *
 * @author Martin Zaloga
 * @deprecated Replaced by TspProblemSolver
 */
public class TspHillClimberTest {

    /**
     * _cities - List of a loaded cities
     * _tour - Index list of cities that make up the path
     * _hc - Object containing hill climber algorithm
     */
    private City[] _cities;
    private Integer[] _tour;
    private HillClimber _hc;

    /**
     * The main trigger method
     * @param args - the argument is the path to the data
     */
    public static void main(String[] args) {
        try {
            new TspHillClimberTest().run(args[0], "my", "greedy", 100, 100);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Function for the run the program
     * @param path - Path where is fyle of data the cities
     * @param clasic - Switching between the classical and improved algorithm Hill-Climber
     * @param switcher - Switching between the Geedy and Random initial solution
     * @param restarts - Numer of repeat optimalizations algorithm
     * @param iteration - Number of iteration algorthm
     */
    public void run(String path, String classic, String switcher, int restarts, int iteration) throws Exception {
        _cities = CityProvider.readCities(path);
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + _cities.length);

        _hc = new HillClimber(new TspObjectiveFunction(_cities), new TspMoveManager(), new TSPSolutionGenerator(switcher, _cities), iteration);
        _hc.startRestartedSearching(classic, restarts);
        TspSolution bestSol = (TspSolution) _hc.getBestSolution();
        _tour = bestSol.getTour();

        Visualizer.instance().createGraph(_cities, _tour, "tsphcgraph.png", 600, 400);
        System.out.println("length: " + bestSol.getObjectiveValue());
    }
}
