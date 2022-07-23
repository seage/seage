/**
 * Class represents the unit metric calculator
 * for each part of the experiment
 * 
 * @author David Omrai
 */

package org.seage.score;

import java.util.List;

import org.seage.score.metric.UnitMetric;

public class ScoreCalculator {

  /**
   * Private constructor to hide default one.
   */
  private ScoreCalculator() {}

  /**
   * Method calculates the score of given objValue.
   * @param optimum Instance optimum.
   * @param greedy Instance lower bound.
   * @param objValue Instance value.
   * @return
   */
  public static double calculateInstanceScore(
      double optimum, double greedy, double objValue) throws Exception {
    return UnitMetric.getMetricValue(optimum, greedy, objValue);
  }

  /**
   * Method calculates weighted mean of given arrays.
   * @param instanceSizes Array of instance names.
   * @param instanceScores Array of instance scores.
   * @return Weighted mean.
   */
  public static double calculateProblemScore(
      List<Double> instanceSizes, double[] instanceScores) throws Exception {
    double numerator = 0.0;
    double denominator = 0.0;

    for (int i = 0; i < instanceSizes.size(); i++) {
      numerator += instanceSizes.get(i) * instanceScores[i];
      denominator += instanceSizes.get(i);
    }
    if (denominator != 0) {
      return numerator / denominator;
    }
    throw new Exception("Dividing by zero.");
  }

  /**
   * Method calculates mean of given array.
   * @param problemsScores Array of problem scores.
   * @return Mean.
   */
  public static double calculateExperimentScore(
      List<Double> problemsScores) {
    return problemsScores.stream().reduce(0.0, Double::sum) / problemsScores.size();
  }

  /**
   * Method calculates score delta.
   * @param initScore Array of problem scores.
   * @param bestScore Array of problem scores.
   * @return Delta score.
   */
  public static double calculateScoreDelta(double initScore, double bestScore) {
    return Math.abs(bestScore - initScore);
  }
}
