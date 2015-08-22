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

import java.util.ArrayList;
import java.util.List;

import org.seage.problem.tsp.City;

/**
 *
 * @author Martin Zaloga
 */
public class TspRandomSolution extends TspSolution
{
    private static final long serialVersionUID = 6408615829159721770L;

    /**
     * Constructor the solution with using the random algorithm for initial solution
     * @param cities - List of cities with their coordinates
     */
    public TspRandomSolution(City[] cities)
    {
        super();
        initRandTour(cities);
    }

    /**
     * Creating the initial random solution
     * @param c - The input list of cities
     */
    private void initRandTour(City[] c)
    {
        List<Integer> listTour = new ArrayList<Integer>();

        /*Moving the cities in ArrayList for a next processing*/
        for (int i = 0; i < c.length; i++)
        {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList<Integer>();
        Integer pom = null;

        /*Cycle the random algorithm*/
        for (int i = 0; i < c.length; i++)
        {
            pom = listTour.get(_rnd.nextInt(listTour.size()));
            randTour.add(pom);
            listTour.remove(pom);
        }

        _tour = new Integer[c.length];

        /*Transform the list of cities, generated wiht random algorithm, in array IDs*/
        for (int i = 0; i < c.length; i++)
        {
            _tour[i] = randTour.get(i);
        }
    }
}
