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

import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.IObjectiveFunction;
import org.seage.metaheuristic.grasp.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author Martin Zaloga
 */
public class TspObjectiveFunction implements IObjectiveFunction
{

    /**
     * _cities - Array of cities
     * _firstIter - Counter of iteration the algorithm
     */
    private City[] _cities;
    private boolean _firstIter;

    /**
     * Constructor the object to assess the steps
     * @param cities - Array of cities
     */
    public TspObjectiveFunction(City[] cities)
    {
        _cities = cities;
        _firstIter = true;
    }

    /**
     * Parameter settings, which says that this is the first iteration
     */
    @Override
    public void reset()
    {
        _firstIter = true;
    }

    /**
     * Function for evaluating the step
     * @param s - Actual solution
     * @param m - The next step
     * @return - Evaluating the step
     */
    @Override
    public double evaluateMove(Solution s, IMove m)
    {
        Integer[] tour = null;
        Integer[] tourBefMod = null;
        double length = 0;
        TspSolution sol = (TspSolution) s;

        tour = sol.getTour().clone();
        TspMove move = (TspMove) m;

        /*Deciding whether to rate the next step or the current solution*/
        if (m != null)
        {

            /*In the first step must be first determine the evaluation*/
            if (_firstIter)
            {
                length = length(tour);
            }
            else
            {
                length = s.getObjectiveValue();
            }

            int ix1 = move.getIx1();
            int ix2 = move.getIx2();
            tourBefMod = tour.clone();
            tour = modify(tour, ix1, ix2);
            length = modifyLength(tourBefMod, tour, ix1, ix2, length);
        }
        else
        {
            length = length(tour);
        }

        _firstIter = false;
        return length;
    }

    /**
     * Execution step the swapping indices of cities
     * @param tour - Current path consisting of the indices cities
     * @param ix1 - Index the first city
     * @param ix2 - Index the second city
     * @return - Modified path
     */
    private Integer[] modify(Integer[] tour, int ix1, int ix2)
    {
        int hlp = tour[ix1];
        tour[ix1] = tour[ix2];
        tour[ix2] = hlp;
        return tour;
    }

    /**
     * Checking whether the index goes not beyond the field
     * @param ind - index what will be Check
     * @return - index what was Checked
     */
    private int controlBigIndex(int ind)
    {
        if (ind == _cities.length)
        {
            ind = 0;
        }
        return ind;
    }

    /**
     * Checking whether the index goes not beyond the field
     * @param ind - index what will be Check
     * @return - index what was Checked
     */
    private int controlSmallIndex(int ind)
    {
        if (ind == -1)
        {
            ind = _cities.length - 1;
        }
        return ind;
    }

    /**
     * Calculation of Euclid distance of neighboring towns
     * @param tour - Path consisting of cities index
     * @param ind - index the neighboring towns
     * @return - Euclid distance
     */
    private double euklDist(Integer[] tour, int ind)
    {
        double dx, dy;
        dx = _cities[tour[ind]].X - _cities[tour[controlBigIndex(ind + 1)]].X;
        dy = _cities[tour[ind]].Y - _cities[tour[controlBigIndex(ind + 1)]].Y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calculate the total length of tour
     * @param tour - Tour consist of indexes of cities
     * @return - Total length of tour
     */
    private double length(Integer[] tour)
    {
        double lenght = 0;
        for (int i = 0; i < tour.length; i++)
        {
            lenght += euklDist(tour, i);
        }
        return lenght;
    }

    /**
     * Calculate the total length of the modify tour
     * @param tourBefMod - Tour before the modification
     * @param tour - Tour after the modification
     * @param ix1 - Index the first city
     * @param ix2 - Index the second city
     * @param lengthBefMod - total length before the modify
     * @return - Length of the modify tour
     */
    private double modifyLength(Integer[] tourBefMod, Integer[] tour, int ix1, int ix2, double lengthBefMod)
    {
        double length = 0, minusDist, plusDist;
        minusDist = euklDist(tourBefMod, controlSmallIndex(ix1 - 1)) + euklDist(tourBefMod, ix1)
                + euklDist(tourBefMod, controlSmallIndex(ix2 - 1)) + euklDist(tourBefMod, ix2);
        plusDist = euklDist(tour, controlSmallIndex(ix1 - 1)) + euklDist(tour, ix1)
                + euklDist(tour, controlSmallIndex(ix2 - 1)) + euklDist(tour, ix2);
        length = lengthBefMod - minusDist + plusDist;
        return length;
    }
}
