
package org.seage.tsp.metaheuristic.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmátlík
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
    private TspSolution _currrentTspSolution;
    
    public void setObjectiveValue(Solution solution)
    {
        _currrentTspSolution = (TspSolution)solution;

        System.out.println("TSPOBJECTFUNCTION: " + solution.getObjectiveValue());
        double distance = 0.0;//Double.MAX_VALUE
        int tourLength = _currrentTspSolution.getTour().length - 1;

        for (int i = 1; i <= tourLength; i++)
            distance += this.getDistance( i , i - 1 );

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
