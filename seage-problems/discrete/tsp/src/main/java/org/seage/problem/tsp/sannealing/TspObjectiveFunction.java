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
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
    private TspSolution _currrentTspSolution;
    
    public void setObjectiveValue(Solution solution)
    {
        _currrentTspSolution = (TspSolution)solution;

        double distance = 0.0;
        int tourLength = _currrentTspSolution.getTour().length - 1;

        for (int i = 1; i <= tourLength; i++)
            distance += this.getDistance( i , i - 1 );

        distance += getDistance(tourLength, 0);

        solution.setObjectiveValue( distance );
    }

  /**
   * Returns the distance between two cities.
   *
   * @param i The first city index.
   * @param j The second city index.
   * @return The distance between the two cities.
   */
  private double getDistance(int i, int j)
  {
    int a = _currrentTspSolution.getTour()[i];
    int b = _currrentTspSolution.getTour()[j];
      
    return getEuclideanDistance (
                _currrentTspSolution.getCities()[a].X,
                _currrentTspSolution.getCities()[a].Y,
                _currrentTspSolution.getCities()[b].X,
                _currrentTspSolution.getCities()[b].Y
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
