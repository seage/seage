package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.runner.LocalExperimentTasksRunner;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 */
public class SingleAlgorithmExperimentTaskEvaluator
    extends SubjectEvaluator<SingleAlgorithmExperimentTaskSubject> {
  private static Logger logger =
      LoggerFactory.getLogger(SingleAlgorithmExperimentTaskEvaluator.class.getName());
  private UUID experimentId;
  private String problemID;
  private List<String> instanceIDs;
  private String algorithmID;
  private long timeoutS;
  private Map<String, ProblemInstanceInfo> instancesInfo;
  private ProblemScoreCalculator problemScoreCalculator;

  protected int stageId;

  // configID -> (instanceID -> objValue)
  protected Map<String, Map<String, Double>> configCache;
  // configID -> (instanceID -> objValue)
  protected Map<String, Map<String, Double>> subjectsObjValues; // TODO: Rename

  /**
   * Constructor.
   *
   * @param experimentId Experiment ID.
   * @param problemID Problem ID.
   * @param instanceIDs Instance IDs.
   * @param algorithmID Algorithm ID.
   * @param timeoutS Timeout [s].
   * @param problemInfo Problem info.
   * @param instancesInfo Instances info.
   */
  public SingleAlgorithmExperimentTaskEvaluator(UUID experimentId, String problemID,
      List<String> instanceIDs, String algorithmID, long timeoutS, ProblemInfo problemInfo,
      Map<String, ProblemInstanceInfo> instancesInfo) {
    super();
    this.experimentId = experimentId;
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.algorithmID = algorithmID;
    this.timeoutS = timeoutS;
    this.configCache = new HashMap<>();
    this.instancesInfo = instancesInfo;
    this.stageId = 0;
    this.problemScoreCalculator = new ProblemScoreCalculator(problemInfo);
  }

  @Override
  public void evaluateSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects)
      throws Exception {
    stageId += 1;

    subjectsObjValues = new HashMap<>();

    HashMap<String, HashMap<String, Integer>> rankedSubjects = new HashMap<>();

    // Create and run tasks for each columns separatly
    for (String instanceID : instanceIDs) {
      List<ExperimentTaskRequest> taskRequests =
          createTaskList(instanceID, subjects, subjectsObjValues);

      logger.info("Evaluating {} configs for instance {}", taskRequests.size(), instanceID);
      // Run tasks
      new LocalExperimentTasksRunner().performExperimentTasks(taskRequests,
          this::reportExperimentTask);
    }

    Map<String, Double> scores = calculateProblemScores(subjectsObjValues);
    Map<String, Map<String, Integer>> rankTable = calculateRankTable(subjectsObjValues);
    Map<String, Double> weightedRanks = calculateWeightedRanks(rankTable);

    setObjValToSubjects(subjects, scores, weightedRanks);

    logger.info("Configs:");
    for (var s : subjects) {
      String configId = s.getAlgorithmParams().hash();
      double score = -s.getObjectiveValue()[0];
      double objVal = s.getObjectiveValue()[1];

      logger.info(" - {}  {}  {}", 
          configId, String.format("%.4f", score), String.format("%7.4f", objVal));
    }
  }

  private void setObjValToSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects,
      Map<String, Double> scores, Map<String, Double> weightedRanks) throws Exception {
    for(var s : subjects) {
      String configID = s.getAlgorithmParams().hash();
      Double score = scores.get(configID);
      Double weightedRank = weightedRanks.get(configID);
      s.setObjectiveValue(new double[] {-score, weightedRank});
    }
  }

  protected synchronized Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      subjectsObjValues.computeIfAbsent(experimentTask.getConfigID(), k -> new HashMap<>());
      subjectsObjValues.get(experimentTask.getConfigID()).put(experimentTask.getInstanceID(),
          experimentTask.getObjValue());

      // Put the objective value of experiment into the configCache
      configCache.computeIfAbsent(experimentTask.getConfigID(), k -> new HashMap<>());
      configCache.get(experimentTask.getConfigID()).put(experimentTask.getInstanceID(),
          experimentTask.getObjValue());

    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s",
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }

  private Map<String, Map<String, Integer>> calculateRankTable(
      Map<String, Map<String, Double>> subjectsObjValues) {
    List<String> configIDs = new ArrayList<>(subjectsObjValues.keySet());

    Map<String, Map<String, Integer>> rankTable = new HashMap<>();

    for (String instanceID : subjectsObjValues.get(configIDs.get(0)).keySet()) {
      // Sort config ids
      Collections.sort(configIDs, (c1, c2) -> (subjectsObjValues.get(c1).get(instanceID)
          .compareTo(subjectsObjValues.get(c2).get(instanceID))));
      int rank = 1;
      for (String configID : configIDs) {
        rankTable.computeIfAbsent(configID, k -> new HashMap<>());
        rankTable.get(configID).put(instanceID, rank++);
      }
    }
    return rankTable;
  }

  private Map<String, Double> calculateWeightedRanks(Map<String, Map<String, Integer>> rankTable)
      throws Exception {
    Map<String, Double> result = new HashMap<>();
    // Each subject evaluate separatly
    for (String configID : rankTable.keySet()) { // NOSONAR
      double sumOfWeights = 0.0;
      double rank = 0.0;
      for (String instanceID : rankTable.get(configID).keySet()) {
        double instanceSize = this.instancesInfo.get(instanceID).getValueDouble("size");
        double instanceRank = rankTable.get(configID).get(instanceID);

        rank += instanceSize * instanceRank;
        sumOfWeights += instanceSize;
      }

      Double configRank = rank / sumOfWeights; // NOSONAR
      result.put(configID, configRank);
    }
    return result;
  }

  /**
   * The method creates the task map from configurations that haven't been used on a given instance.
   *
   * @param subjects Algoritm configurations.
   * @param instanceID Instance id.
   * @return task map.
   * @throws Exception Exception.
   */
  protected List<ExperimentTaskRequest> createTaskList(String instanceID,
      List<SingleAlgorithmExperimentTaskSubject> subjects,
      Map<String, Map<String, Double>> subjectsObjValues) throws Exception {
    List<ExperimentTaskRequest> taskList = new ArrayList<>();

    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      // Calculate the subject hash
      AlgorithmParams algorithmParams = subject.getAlgorithmParams();
      String configId = algorithmParams.hash();

      if (configCache.containsKey(configId) && configCache.get(configId).containsKey(instanceID)) {
        subjectsObjValues.computeIfAbsent(configId, k -> new HashMap<>());
        subjectsObjValues.get(configId).put(instanceID, configCache.get(configId).get(instanceID));
      } else {
        ExperimentTaskRequest task = new ExperimentTaskRequest(UUID.randomUUID(), this.experimentId,
            1, this.stageId, this.problemID, instanceID, this.algorithmID, configId,
            algorithmParams, null, this.timeoutS);
        taskList.add(task);
      }
    }
    return taskList;
  }

  protected Map<String, Double> calculateProblemScores(
      Map<String, Map<String, Double>> subjectsObjValues) throws Exception {
    Map<String, Double> result = new HashMap<>();
    for (String configID : subjectsObjValues.keySet()) { // NOSONAR
      Map<String, Double> objValues = subjectsObjValues.get(configID);
      double score = calculateProblemScore(configID, objValues);
      result.put(configID, score);
    }
    return result;
  }

  /**
   * Method return calculated problem score for configs score over all instances.
   *
   * @param configID ConfigID.
   * @return ProblemScore
   * @throws Exception Exception.
   */
  protected double calculateProblemScore(String configID, Map<String, Double> objValues) throws Exception {
    List<Double> instanceScores = new ArrayList<>();
    for (String instanceID : instanceIDs) {
      instanceScores.add(problemScoreCalculator.calculateInstanceScore(instanceID, objValues.get(instanceID)));
    }

    return problemScoreCalculator.calculateProblemScore(
        instanceIDs.toArray(new String[]{}),
        instanceScores.stream().mapToDouble(a -> a).toArray());
  }

  @Override
  protected double[] evaluate(SingleAlgorithmExperimentTaskSubject solution) throws Exception {
    throw new UnsupportedOperationException("Should be unimplemented");
  }
}
