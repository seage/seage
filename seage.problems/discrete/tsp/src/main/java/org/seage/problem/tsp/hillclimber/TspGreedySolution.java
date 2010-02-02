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

import java.util.ArrayList;
import java.util.List;
import org.seage.problem.tsp.City;

/**
 *
 * @author Martin Zaloga
 */
public class TspGreedySolution extends TspSolution {

    /**
     * Constructor the solution with using the greedy algorithm for initial solution
     * @param cities - List of cities with their coordinates
     */
    public TspGreedySolution(City[] cities) {
        super();
        initGreedyTour(cities, _rnd.nextInt(cities.length));
    }

    /**
     * Creating the initial greedy solution
     * @param c - The input list of cities
     * @param listIndex - index the city to have to be the starting point
     * @return - Total distance the initial greedy solution
     */
    private double initGreedyTour(City[] c, int listIndex) {
        List<City> openList = new ArrayList();

        /*Moving the cities in ArrayList for a next processing*/
        for (int i = 0; i < c.length; i++) {
            openList.add(c[i]);
        }

        List<City> hungerTour = new ArrayList();
        City actCity = openList.get(listIndex);
        hungerTour.add(actCity);
        openList.remove(listIndex);
        double totalDist = 0;

        /*Cycle the greedy algorithm*/
        while (openList.size() != 0) {
            double distance = Double.MAX_VALUE, aktDistance;
            for (int i = 0; i < openList.size(); i++) {
                aktDistance = euklDist(openList.get(i), actCity);
                if (aktDistance < distance) {
                    distance = aktDistance;
                    listIndex = i;
                }
            }
            actCity = openList.get(listIndex);
            hungerTour.add(actCity);
            openList.remove(listIndex);
            totalDist += distance;
        }

        totalDist += euklDist(hungerTour.get(0), hungerTour.get(hungerTour.size() - 1));
        _tour = new Integer[c.length];

        /*Transform the list of cities, generated wiht greedy algorithm, in array IDs*/
        for (int i = 0; i < c.length; i++) {
            _tour[i] = hungerTour.get(i).ID - 1;
        }

        return totalDist;
    }

    /**
     * Calculation of Euclid distance of two towns
     * @param c1 - First city
     * @param c2 - Secon city
     * @return - Euclid distance of two towns
     */
    private double euklDist(City c1, City c2) {
        double dx = c1.X - c2.X;
        double dy = c1.Y - c2.Y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
