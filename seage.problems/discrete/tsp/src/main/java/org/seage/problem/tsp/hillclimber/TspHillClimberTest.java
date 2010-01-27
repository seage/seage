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
     */
    private City[] _cities;

    public static void main(String[] args) {
        try {
            new TspHillClimberTest().run(args[0], "my", "greedy", 100, 1000);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Functions to enable the Hill-Climber algorithm
     * @param clasic - Switching between the classical and improved algorithm Hill-Climber
     * @param switcher - Switching between the Geedy and Random initial solution
     * @param hc - Object that implements the Hill-Climber Algorithm
     * @return - Sequence IDs of cities, which define a tour
     */
    private Integer[] startHC(String clasic, String switcher, HillClimber hc) {
        Integer[] tour = null;
        TspSolution tspSolution = null;

        /*Initialization for the random initial solution*/
        if (switcher.equals("Random") || switcher.equals("random")) {
            tspSolution = new TspRandomSolution(_cities);
        }

        /*Initialization for the greedy initial solution*/
        if (switcher.equals("Greedy") || switcher.equals("greedy")) {
            tspSolution = new TspGreedySolution(_cities);
        }

        hc.startSearching(tspSolution, clasic);
        //hc.startRestartedSearching(tspSolution, clasic, 100);
        tour = tspSolution.getTour();

        return tour;
    }

    /**
     * Function for the
     * @param path - Path where is fyle of data the cities
     * @param clasic - Switching between the classical and improved algorithm Hill-Climber
     * @param switcher - Switching between the Geedy and Random initial solution
     * @param restarts - Numer of repeat optimalizations algorithm
     * @param iteration - Number of iteration algorthm
     */
    public void run(String path, String clasic, String switcher, int restarts, int iteration) throws Exception {
        _cities = CityProvider.readCities(path);
        Integer[] tour = null;
        HillClimber hc = null;
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + _cities.length);
        double bestDist = Double.MAX_VALUE;
        Solution bestSolution = null;
        int countRest = 0;

        /*Repeat the algorithm with reseting*/
        while (countRest <= restarts) {
            hc = new HillClimber(new TspObjectiveFunction(_cities), new TspMoveManager());
            hc.setIterationCount(iteration);
            tour = startHC(clasic, switcher, hc);

            /*Choosing the best solution*/
            if (bestDist > hc.getBestSolution().getObjectiveValue()) {
                bestDist = hc.getBestSolution().getObjectiveValue();
                bestSolution = hc.getBestSolution();
            }

            countRest++;
        }

        Visualizer.instance().createGraph(_cities, tour, "tsphcgraph.png", 600, 400);
        System.out.println("length: " + bestSolution.getObjectiveValue());
    }
}
