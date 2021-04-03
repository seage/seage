package org.seage.sandbox.metrics;

public class UnitMetric {
  private static double INTERVAL_MIN = 0.0;
  private static double INTERVAL_MAX = 1.0;

  /**
   * Method returns the metric based on given data.
   * 
   * @param upperBound The value of random generator.
   * @param lowerBound The optimal value.
   * @param current    Input value for metric.
   * @return The metric for given value.
   */
  public  static double getMetricValue(double lowerBound, double upperBound, double current)
      throws Exception {
    if (upperBound < 0 || lowerBound < 0 || current < 0) {
      throw new Exception("Bad input values: input parameter < 0");
    }
    if (upperBound < lowerBound) {
      throw new Exception("Bad input values: upperBound < lowerBound");
    }
    if (current < lowerBound) {
      throw new Exception("Bad input values: value can't be better than optimum");
    }

    return INTERVAL_MAX - mapToInterval(lowerBound, Math.max(upperBound, current), current);
  }

  /**
   * Method maps the value of one interval onto a new one.
   * 
   * @param lowerBound    Lower value of the first interval.
   * @param upperBound    Upper value of the first interval.
   * @param value         Value to map to a new interval.
   * @return Return the mapped value of the value.
   */
  private static double mapToInterval(double lowerBound, double upperBound, double value) 
      throws Exception {
    double valueNormalization = (value - lowerBound) / (upperBound - lowerBound);
    double scaling = valueNormalization * (INTERVAL_MAX - INTERVAL_MIN);
    double shifting = scaling + INTERVAL_MIN;

    return shifting;
  }
}
