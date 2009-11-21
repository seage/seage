
package org.seage.metaheuristic.sannealing;

import org.seage.metaheuristic.sannealing.ISimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulateAnnealing;
//import org.seage.tsp.data.City;

/**
 *
 * @author Jan Zmátlík
 */
public abstract class OldTspClient implements ISimulatedAnnealing {

    /**
   * The cities to be visited.
   */
  //private City cities[];

  /**
   * The simulated annealing worker class.
   */
  private SimulateAnnealing simulateAnnealing;

  private double maximalTemperature = 100;

  private double minimalTemperature = 1.778;

  private double annealingCoefficient = 0.88;

  private String message;

//  public TspClient(City cities[])
//  {
//    this.cities = cities;
//  }

  /**
   * Returns the starting temperature for the
   * annealing process.
   *
   * @return The starting temperature for the annealing process.
   */

  public double getMaximalTemperature()
  {
    return this.maximalTemperature;
  }

  /**
   * Returns the minimal temperature for the
   * annealing process.
   *
   * @return The minimal temperature for the annealing process.
   */
  public double getMinimalTemperature()
  {
    return this.minimalTemperature;
  }

  /**
   * Return annealing coefficient (Alpha).
   *
   * @return Return annealing coefficient (Alpha).
   */
  public double getAnnealingCoefficient()
  {
    return this.annealingCoefficient;
  }

    /**
   * Returns the distance between two cities.
   *
   * @param i The first city index.
   * @param j The second city index.
   * @return The distance between the two cities.
   */
//  public double getDistance(int i, int j)
//  {
//    int c1 = this.simulateAnnealing.order[i % cities.length];
//    int c2 = this.simulateAnnealing.order[j % cities.length];
//
//    return getEuclideanDistance (
//                cities[c1].X,
//                cities[c1].Y,
//                cities[c2].X,
//                cities[c2].Y
//            );
//  }

  private double getEuclideanDistance(double x, double y, double x1, double y1)
  {
      return Math.sqrt( Math.pow( (x1 - x) , 2 ) + Math.pow( (y1 - y) , 2 ) );
  }

//  public int getCount()
//  {
//      return this.cities.length;
//  }

  public void run()
  {
    this.simulateAnnealing = new SimulateAnnealing( this );
    this.simulateAnnealing.run();
  }

  public void update()
  {
      System.out.println(this.message);
  }

  public void setMessage(String message)
  {
      this.message = message;
      this.update();
  }

  /**
   * Return the length of the current path through
   * the cities.
   *
   * @return The length of the current path through the cities.
   */
//  public double length()
//  {
//    double distance = 0.0;
//    for (int i = 1; i <= this.getCount(); i++)
//      distance += this.getDistance(i, i - 1);
//    return distance;
//  }

//    public double getSolution()
//    {
//        return this.length();
//    }

}
