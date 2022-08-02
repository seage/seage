/**
 * Class represents the Unit metric
 * 
 * @author David Omrai
 */

package org.seage.score.metric;


public class UnitMetric {
  private static final double INTERVAL_MIN = 0.0;
  private static final double INTERVAL_MAX = 1.0;

  /**
   * Private constructor to hide default one.
   */
  private UnitMetric() {}

  /**
   * Method returns the metric based on given data.
   * 
   * @param upperBound The value of greedy algorithm.
   * @param lowerBound The optimal value.
   * @param current    Input value for metric.
   * @return The metric for given value.
   */
  public static double getMetricValue(double lowerBound, double upperBound, double current)
      throws Exception {
    if (upperBound < 0 || lowerBound < 0 || current < 0) {
      throw new Exception("Bad input values: input parameter < 0");
    }
    if (upperBound < lowerBound) {
      throw new Exception("Bad input values: upperBound < lowerBound");
    }
    if (current < lowerBound) {
      throw new Exception("Bad input values: value can't be better than optimum: " + current);
    }

    return INTERVAL_MAX - mapToInterval(lowerBound, upperBound, Math.min(upperBound, current));
  }

  /**
   * Method maps the value of one interval onto a new one.
   * 
   * @param lowerBound Lower value of the first interval.
   * @param upperBound Upper value of the first interval.
   * @param value      Value to map to a new interval.
   * @return Return the mapped value of the value.
   */
  private static double mapToInterval(double lowerBound, double upperBound, double value)
      throws ArithmeticException {
    double valueNormalization = (value - lowerBound) / (upperBound - lowerBound);
    double scaling = valueNormalization * (INTERVAL_MAX - INTERVAL_MIN);
    // Shifting
    return scaling + INTERVAL_MIN;
  }
}
