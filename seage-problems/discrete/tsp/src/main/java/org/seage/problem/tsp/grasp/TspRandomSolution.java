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
package org.seage.problem.tsp.grasp;

import java.util.ArrayList;
import java.util.List;
import org.seage.problem.tsp.City;

/**
 *
 * @author Martin Zaloga
 */
public class TspRandomSolution extends TspSolution {

    /**
     * Constructor the solution with using the random algorithm for initial solution
     * @param cities - List of cities with their coordinates
     */
    public TspRandomSolution(City[] cities) {
        super();
        initRandTour(cities);
    }

    /**
     * Creating the initial random solution
     * @param c - The input list of cities
     */
    private void initRandTour(City[] c) {
        List<Integer> listTour = new ArrayList();

        /*Moving the cities in ArrayList for a next processing*/
        for (int i = 0; i < c.length; i++) {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList();
        Integer pom = null;

        /*Cycle the random algorithm*/
        for (int i = 0; i < c.length; i++) {
            pom = listTour.get(_rnd.nextInt(listTour.size()));
            randTour.add(pom);
            listTour.remove(pom);
        }

        _tour = new Integer[c.length];

        /*Transform the list of cities, generated wiht random algorithm, in array IDs*/
        for (int i = 0; i < c.length; i++) {
            _tour[i] = randTour.get(i);
        }
    }
}
