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

import java.util.Comparator;
import org.seage.metaheuristic.particles.IObjectiveFunction;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TourProvider;
import org.seage.problem.tsp.TspPhenotypeEvaluator;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
    private City[] _cities;

    public TspObjectiveFunction(City[] cities)
    {
        _cities = cities;
    }

    public void setObjectiveValue(Particle particle)
    {
        TspParticle currentParticle = ((TspParticle)particle);
        double distance = 0.0;
        Integer[] tour = currentParticle.getTour();
        int tourLength = tour.length;

        try
        {
            distance = (int)TourProvider.getTourLenght(tour, _cities);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        //insertionSort(particle.getCoords());

//        for (int i = 1; i <= tourLength; i++)
//            distance += this.getDistance( i , i - 1 );
//
//        distance += getDistance(tourLength, 0);

        particle.setEvaluation( distance );
    }

//    public static double[] transformCoords(double[] coords)
//    {
//        return null;
//    }

    

//    private void insertionSort(double[] array)
//    {
//        //double[] sortedArray = array.clone();
//        for(int i = 0; i < array.length - 1; i++)
//        {
//            int j = i + 1;
//            double tmp = array[j];
//            int tmpIndex = _currentParticle.getTour()[j];
//            while(j > 0 && tmp > array[j-1])
//            {
//                array[j] = array[j-1];
//                _currentParticle.getTour()[j] = _currentParticle.getTour()[j-1];
//                j--;
//            }
//            array[j] = tmp;
//           _currentParticle.getTour()[j] = tmpIndex;
//        }
//    }

  /**
   * Returns the distance between two cities.
   *
   * @param i The first city index.
   * @param j The second city index.
   * @return The distance between the two cities.
   */
//  private double getDistance(int i, int j)
//  {
//    int a = _currentParticle.getTour()[i];
//    int b = _currentParticle.getTour()[j];
//
//    return getEuclideanDistance (
//                _currentParticle.getCities()[a].X,
//                _currentParticle.getCities()[a].Y,
//                _currentParticle.getCities()[b].X,
//                _currentParticle.getCities()[b].Y
//            );
//  }

  /**
   * Returns the Euclidean distance between two points (every point is represented by x-axis and y-axis).
   *
   * @param x The first point x-axis
   * @param y The first point y-axis
   * @param x1 The second point x-axis
   * @param y1 The second point y-axis
   * @return The distance between the two points.
   */
//  private double getEuclideanDistance(double x, double y, double x1, double y1)
//  {
//      return Math.sqrt( Math.pow( (x1 - x) , 2 ) + Math.pow( (y1 - y) , 2 ) );
//  }

}
