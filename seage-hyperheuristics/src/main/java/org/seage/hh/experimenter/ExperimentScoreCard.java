/**
 * @author David Omrai
 */

package org.seage.hh.experimenter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExperimentScoreCard {
  /**
   * Name of the algorithm.
   */
  String algorithmName;

  /**
   * Total algorithm score.
   */
  double totalScore;

  /**
   * String: problemId - String: instanceId - Double: instanceId value.
   */
  Map<String, Map<String, Double>> scorePerInstance;

  /**
   * . String: problemId - Double: problemId score
   */
  Map<String, Double> scorePerProblem;


  /**
   * Constructor, sets all necessary parameters.
   * 
   * @param algorithmName Name of the algorithm.
   * @param problems       Array of problem names.
   */
  public ExperimentScoreCard(String algorithmName, String[] problems) {
    this.algorithmName = algorithmName;

    scorePerProblem = new HashMap<>();
    scorePerInstance = new HashMap<>();  

    for (String problem : problems) {
      scorePerInstance.put(problem, new HashMap<>());
      scorePerProblem.put(problem, null);
    }
  }


  /**
   * Method stores given instanceId value.
   * 
   * @param problemId  Name of the problem.
   * @param instanceId Name of the problem instance.
   * @param value      Value of the instance.
   * @return Returns this.
   */
  public ExperimentScoreCard putInstanceScore(String problemId, String instanceId, Double value) {
    scorePerInstance.get(problemId).put(instanceId, value);
    return this;
  }


  /**
   * Method stores given problemId value.
   * 
   * @param problemId Name of the problem.
   * @param value     Value of the problem.
   * @return Returns this.
   */
  public ExperimentScoreCard putProblemScore(String problemId, Double value) {
    scorePerProblem.put(problemId, value);
    return this;
  }

 
  /**
   * Method sets algorithm score.
   * 
   * @param score Algorithm score.
   */
  public void setAlgorithmScore(Double score) {
    totalScore = score;
  }


  /**
   * Method returns the problem score.
   * 
   * @return Problem problem score.
   */
  public double getAlgorithmScore() {
    return totalScore;
  }


  /**
   * Method returns the name of the algorithm.
   * 
   * @return Name of the algorithm.
   */
  public String getName() {
    return algorithmName;
  }


  /**
   * Method returns value of given instance.
   * 
   * @param problemId  Name of the problem doamain.
   * @param instanceId Name of the intance.
   * @return Returns the value of given instacne.
   */
  public double getInstanceScore(String problemId, String instanceId) {
    return scorePerInstance.get(problemId).get(instanceId);
  }


  /**
   * Returns the value of given problem.
   * 
   * @param problemId Name of a problem.
   * @return Returns the value of a problem.
   */
  public double getProblemScore(String problemId) {
    return scorePerProblem.get(problemId);
  }

  /**
   * Method returns the set of problems scores
   * 
   * @return Returns the set of problems scores
   */
  public Set<Double> getProblemsScores() {
    return new HashSet<>(scorePerProblem.values());
  }


  /**
   * Method returns the set of problems names.
   * 
   * @return Set of problem names.
   */
  public Set<String> getProblems() {
    return scorePerProblem.keySet();
  }


  /**
   * Method returns the set of problem names.
   * 
   * @param problemId Set of problem names.
   * @return
   */
  public Set<String> getInstances(String problemId) {
    return scorePerInstance.get(problemId).keySet();
  }
}
