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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;

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
    protected City[] _cities;

    public TspSolution(City[] cities)
    {
        _tour = new Integer[ cities.length ];
        _cities = cities;
    }

    public Integer[] getTour()
    {
        return _tour;
    }

     public void setTour(Integer[] tour)
    {
        _tour = tour;
    }

    public City[] getCities()
    {
        return _cities;
    }
    
    public void setCities(City[] cities)
    {
        _cities = cities;
    }
    
    @Override
    public String toString()
    {
    	String res = new String();
    	for(Integer t : _tour)
    		res += t + " ";
    	return res;
    }

    @Override
    public TspSolution clone()
    {
        TspSolution tspSolution = null;
        try
        {
            tspSolution = (TspSolution)super.clone();
            tspSolution.setTour( _tour.clone() );
            tspSolution.setCities( _cities );
            tspSolution.setObjectiveValue(getObjectiveValue());
        } catch (Exception ex)
        {
            Logger.getLogger(TspSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tspSolution;
    }

}
