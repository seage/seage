package org.seage.aal.problem;

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

    double numerator = 0.0;
    double denominator = 0.0;
    for (int i = 0; i < instanceIDs.length; i++) {
      // Weight
      double instanceSize = problemInfo
          .getProblemInstanceInfo(instanceIDs[i]).getValueDouble("size");
      
      numerator += instanceSize * instanceScores[i];
      denominator += instanceSize;
    }

    return numerator / denominator;
  }
}