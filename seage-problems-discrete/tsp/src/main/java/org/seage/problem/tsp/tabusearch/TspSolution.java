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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.tabusearch;

import org.seage.metaheuristic.tabusearch.SolutionAdapter;
import org.seage.problem.tsp.City;

/**
 *
 * @author Richard Malek
 */
public class TspSolution extends SolutionAdapter
{
    protected Integer[] _tour;

    public TspSolution()
    {
    } // Appease clone()

    public TspSolution(City[] customers)
    {
        // Crudely initialize solution
        _tour = new Integer[customers.length];
        for (int i = 0; i < customers.length; i++)
            _tour[i] = i + 1;
    } // end constructor

    @Override
    public Object clone()
    {
        TspSolution copy = (TspSolution) super.clone();
        copy._tour = this._tour.clone();
        return copy;
    } // end clone

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
        return String.format("%f - %d", getObjectiveValue()[0], hashCode());
    } // end toString

} // end class MySolution
