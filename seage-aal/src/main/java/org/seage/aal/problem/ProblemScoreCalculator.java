package org.seage.aal.problem;

import java.util.List;
import org.seage.aal.problem.metrics.UnitMetric;

public class ProblemScoreCalculator {

  private ProblemInfo problemInfo;

  public ProblemScoreCalculator(ProblemInfo problemInfo) {
    this.problemInfo = problemInfo;
  }

  /**
   * Method calculates the score of given objValue.
   * @param instanceID Instance name.
   * @param objValue Instance value.
   * @return Calculated score.
   */
  public double calculateInstanceScore(String instanceID, double objValue) throws Exception {
    double optimum = problemInfo.getProblemInstanceInfo(instanceID).getValueDouble("optimum");
    double random = problemInfo.getProblemInstanceInfo(instanceID).getValueDouble("random");
    return UnitMetric.getMetricValue(optimum, random, objValue);
  }

  /**
   * Method calculates weighted mean of given arrays.
   * @param instanceIDs Array of instance names.
   * @param instanceScores Array of instance scores.
   * @return Weighted mean.
   */
  public double calculateProblemScore(String[] instanceIDs, double[] instanceScores)
      throws Exception {
    if (instanceIDs.length != instanceScores.length) {
      throw new Exception("Bad input values: input arrays have different lenght");
    }

    if (instanceIDs.length == 0) {
      throw new Exception("Error: inscante IDs array is empty.");
    }

    double numerator = 0.0;
    double denominator = 0.0;
    for (int i = 0; i < instanceIDs.length; i++) {
      // Weight
      double instanceSize = problemInfo
          .getProblemInstanceInfo(instanceIDs[i]).getValueDouble("size");
      
      numerator += instanceSize * instanceScores[i];
      denominator += instanceSize;
    }

    if (denominator != 0) {
      return numerator / denominator;
    }

    throw new Exception("Error: dividing by zero."); 
  }

  /**
   * Method calculates mean of given array.
   * @param problemsScores Array of problem scores.
   * @return Mean.
   */
  public static double calculateExperimentScore(List<Double> problemsScores) {
    return problemsScores.stream().reduce(0.0, Double::sum) / problemsScores.size();
  }

  /**
   * Method calculates score delta.
   * @param initScore Array of problem scores.
   * @param bestScore Array of problem scores.
   * @return Delta score.
   */
  public double calculateScoreDelta(double initScore, double bestScore) {
    return Math.abs(bestScore - initScore);
  }
}