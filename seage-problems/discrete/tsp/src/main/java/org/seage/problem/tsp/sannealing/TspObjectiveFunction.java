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

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
    //private TspSolution _currrentTspSolution;
    protected City[] _cities;
    
    public TspObjectiveFunction(City[] cities)
    {
    	_cities = cities;
    }
    
    public double getObjectiveValue(Solution solution)
    {
    	TspSolution currrentTspSolution = (TspSolution)solution;

        double distance = 0.0;
        int tourLength = currrentTspSolution.getTour().length - 1;

        for (int i = 1; i <= tourLength; i++)
            distance += this.getDistance(currrentTspSolution, i , i - 1 );

        distance += getDistance(currrentTspSolution, tourLength, 0);

        return distance;
    }

  /**
   * Returns the distance between two cities.
   *
   * @param i The first city index.
   * @param j The second city index.
   * @return The distance between the two cities.
   */
  private double getDistance(TspSolution tspSolution, int i, int j)
  {
    int a = tspSolution.getTour()[i];
    int b = tspSolution.getTour()[j];
      
    return getEuclideanDistance (
    		_cities[a].X,
    		_cities[a].Y,
    		_cities[b].X,
    		_cities[b].Y
            );
  }
  
  /**
   * Returns the Euclidean distance between two points (every point is represented by x-axis and y-axis).
   *
   * @param x The first point x-axis
   * @param y The first point y-axis
   * @param x1 The second point x-axis
   * @param y1 The second point y-axis
   * @return The distance between the two points.
   */
  private double getEuclideanDistance(double x, double y, double x1, double y1)
  {
      return Math.sqrt( Math.pow( (x1 - x) , 2 ) + Math.pow( (y1 - y) , 2 ) );
  }

}
