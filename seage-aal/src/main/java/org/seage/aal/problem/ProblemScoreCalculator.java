package org.seage.aal.problem;

import java.util.ArrayList;
import java.util.List;

import org.seage.score.ScoreCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProblemScoreCalculator {
  private static Logger logger = LoggerFactory.getLogger(ProblemScoreCalculator.class.getName());

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
    try {
      double optimum = problemInfo.getProblemInstanceInfo(instanceID).getValueDouble("optimum");
      double greedy = problemInfo.getProblemInstanceInfo(instanceID).getValueDouble("greedy");

      return ScoreCalculator.calculateInstanceScore(optimum, greedy, objValue);
    } catch (NumberFormatException ex) {
      return 0;
    }
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
      throw new Exception("InstanceIDs size is 0.");
    }

    List<Double> instanceSizes = new ArrayList<>();

    for (int i = 0; i < instanceIDs.length; i++) {
      // Weight
      double instanceSize = problemInfo
          .getProblemInstanceInfo(instanceIDs[i]).getValueDouble("size");
      instanceSizes.add(instanceSize);
      if (instanceSize == 0) {
        throw new Exception("InstanceSize is 0");
      }
    }

    return ScoreCalculator.calculateProblemScore(instanceSizes, instanceScores);
  }

  /**
   * Method calculates mean of given array.
   * @param problemsScores Array of problem scores.
   * @return Mean.
   */
  public static double calculateExperimentScore(List<Double> problemsScores) {
    return ScoreCalculator.calculateExperimentScore(problemsScores);
  }

  /**
   * Method calculates score delta.
   * @param initScore Array of problem scores.
   * @param bestScore Array of problem scores.
   * @return Delta score.
   */
  public double calculateScoreDelta(double initScore, double bestScore) {
    return ScoreCalculator.calculateScoreDelta(initScore, bestScore);
  }
}