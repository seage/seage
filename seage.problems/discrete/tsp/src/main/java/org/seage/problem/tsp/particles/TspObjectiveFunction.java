/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.particles;

import org.seage.metaheuristic.particles.IObjectiveFunction;
import org.seage.metaheuristic.particles.Particle;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
    TspParticle _currentParticle = null;
    public void setObjectiveValue(Particle particle)
    {
        _currentParticle = ((TspParticle)particle);
        double distance = 0.0;
        int tourLength = _currentParticle.getTour().length - 1;

        for (int i = 1; i <= tourLength; i++)
            distance += this.getDistance( i , i - 1 );

        distance += getDistance(tourLength, 0);

        particle.setEvaluation( distance );
    }

    public static double[] transformCoords(double[] coords)
    {
        return null;
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
    int a = _currentParticle.getTour()[i];
    int b = _currentParticle.getTour()[j];

    return getEuclideanDistance (
                _currentParticle.getCities()[a].X,
                _currentParticle.getCities()[a].Y,
                _currentParticle.getCities()[b].X,
                _currentParticle.getCities()[b].Y
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
