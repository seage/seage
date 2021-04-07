package org.seage.aal.problem;

import java.util.Arrays;
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

  public double calculateProblemScore(String[] instanceIDs, double[] instanceScores) {
    return Arrays.stream(instanceScores).sum() / instanceScores.length;
  }
}