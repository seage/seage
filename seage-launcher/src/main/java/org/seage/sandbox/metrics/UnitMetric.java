package org.seage.sandbox.metrics;

import java.util.List;

public class UnitMetric {
  private static double scoreIntervalFrom = 0.0;
  private static double scoreIntervalTo = 1.0;

  /**
   * Method returns the metric based on given data.
   * 
   * @param upperBound The value of random generator.
   * @param lowerBound The optimal value.
   * @param current    Input value for metric.
   * @return The metric for given value.
   */
  public  static double getMetric(double lowerBound, double upperBound, double current)
      throws Exception {
    if (upperBound < 0 || lowerBound < 0 || current < 0) {
      throw new Exception("Bad input values: input parameter < 0");
    }
    if (upperBound < lowerBound) {
      throw new Exception("Bad input values: upperBound < lowerBound");
    }
    if (current < lowerBound) {
      throw new Exception("Bad input values: current is not from interval");
    }

    return scoreIntervalTo
        - (mapToInterval(
          lowerBound, Math.min(upperBound, current), scoreIntervalFrom, scoreIntervalTo, current));
  }

  /**
   * Method maps the value of one interval onto a new one.
   * 
   * @param lowerBound    Lower value of the first interval.
   * @param upperBound    Upper value of the first interval.
   * @param intervalLower Lower value of the new interval.
   * @param intervalUpper Upper value of the new interval.
   * @param value         Value to map to a new interval.
   * @return Return the mapped value of the value.
   */
  private static double mapToInterval(double lowerBound, double upperBound, double intervalLower,
      double intervalUpper, double value) throws Exception {
    double valueNormalization = (value - lowerBound) / (upperBound - lowerBound);
    double scaling = valueNormalization * (intervalUpper - intervalLower);
    double shifting = scaling + intervalLower;

    return shifting;
  }
}
