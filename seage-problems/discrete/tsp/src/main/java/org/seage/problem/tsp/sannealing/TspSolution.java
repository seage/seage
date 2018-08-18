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
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.sannealing;


import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public abstract class TspSolution extends Solution
{

    /**
     * Represent order of cities
     */
    protected Integer[] _tour;

    /**
     * Array of cities
     */

    public TspSolution(int length)
    {
        _tour = new Integer[length];
    }

    public Integer[] getTour()
    {
        return _tour;
    }

    public void setTour(Integer[] tour)
    {
        _tour = tour;
    }

    @Override
    public String toString()
    {
        String res = new String();
        for (Integer t : _tour)
            res += t + " ";
        return res;
    }

    @Override
    public TspSolution clone()
    {
        TspSolution tspSolution = null;

        tspSolution = (TspSolution) super.clone();
        tspSolution.setTour(_tour.clone());
        tspSolution.setObjectiveValue(getObjectiveValue());

        return tspSolution;
    }

}
