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
  private Function<ExperimentTaskRecord, Void> reportFn;
  private Map<String, ProblemInstanceInfo> instancesInfo;
  private ProblemScoreCalculator problemScoreCalculator;

  protected int stageId;

  // configID -> objValue
  protected Map<String, Map<String, Double>> configCache;
  // configID -> (instanceID -> objValue)
  protected Map<String, Map<String, Double>> subjectsObjValues;

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
   * @param reportFn Report function.
   */
  public SingleAlgorithmExperimentTaskEvaluator(UUID experimentId, String problemID,
      List<String> instanceIDs, String algorithmID, long timeoutS, ProblemInfo problemInfo,
      Map<String, ProblemInstanceInfo> instancesInfo,
      Function<ExperimentTaskRecord, Void> reportFn) {
    super();
    this.experimentId = experimentId;
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.algorithmID = algorithmID;
    this.timeoutS = timeoutS;
    this.configCache = new HashMap<>();
    this.instancesInfo = instancesInfo;
    this.reportFn = reportFn;
    this.stageId = 0;
    this.problemScoreCalculator = new ProblemScoreCalculator(problemInfo);
  }

  @Override
  public void evaluateSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects)
      throws Exception {
    stageId += 1;

    for (var s : subjects) {
      logger.info(s.getAlgorithmParams().hash());
    }

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

      // Rank subjects
      // rankedSubjects.put(instanceID,
      // getRankedSubjectsObjValue(subjects, instanceID));
    }
    // Set the configuration objective value
    // SingleAlgorithmExperimentTaskSubject bestSubject =
    // setConfigRankToSubjects(subjects, rankedSubjects);

    // reportBestSubjectProblemscore(bestSubject);
    Map<String, Map<String, Integer>> rankTable = calculateRankTable(subjectsObjValues);
    Map<String, Double> weightedRanks = calculateWeightedRanks(rankTable);
    setObjValToSubjects(subjects, weightedRanks);
    int a = 0;
  }

  private void setObjValToSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects,
      Map<String, Double> weightedRanks) throws Exception {
    for(var s : subjects) {
      s.setObjectiveValue(new double[] {weightedRanks.get(s.getAlgorithmParams().hash())});
    }
  }

  protected Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
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
  private List<ExperimentTaskRequest> createTaskList(String instanceID,
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

  /**
   * Method evaluates given subject.
   *
   * @param subjects Subjects.
   * @param rankedSubjects Ranked subjects map.
   * @return Weighted order.
   * @throws Exception Exception.
   */
  protected SingleAlgorithmExperimentTaskSubject setConfigRankToSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects,
      HashMap<String, HashMap<String, Integer>> rankedSubjects) throws Exception {

    double bestSubjectRank = Double.MAX_VALUE;
    SingleAlgorithmExperimentTaskSubject bestSubject = null;
    // Each subject evaluate separatly
    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      String configID = subject.getAlgorithmParams().hash();
      double sumOfWeights = 0.0;
      double result = 0.0;
      for (String instanceID : this.instanceIDs) {
        double instanceSize = this.instancesInfo.get(instanceID).getValueDouble("size");
        double instanceRank = rankedSubjects.get(instanceID).get(configID);

        result += instanceSize * instanceRank;
        sumOfWeights += instanceSize;
      }
      if (sumOfWeights == 0.0) {
        throw new Exception("Error: dividing by zero.");
      }
      // Config score
      Double configRank = result / sumOfWeights;
      subject.setObjectiveValue(new double[] {configRank});

      if (configRank < bestSubjectRank) {
        bestSubject = subject;
        bestSubjectRank = configRank;
      }
    }

    // Returns the best config id
    return bestSubject;
  }

  /**
   * Method reports the experiment config score.
   *
   * @param bestSubject Config subject.
   * @throws Exception Exception.
   */
  // protected void reportBestSubjectProblemscore(
  // SingleAlgorithmExperimentTaskSubject bestSubject) throws Exception {
  // String bestConfigID = bestSubject.getAlgorithmParams().hash();
  // double problemScore = getProblemScore(bestConfigID);

  // logger.info(String.format("Best overall configuration %-10.10s confScore %.4g score %.4g",
  // bestConfigID, bestSubject.getObjectiveValue()[0], problemScore));

  // ExperimentTaskRecord customRecord =
  // this.configCache.get(bestConfigID).get(this.instanceIDs.get(0));

  // customRecord.setScore(problemScore);
  // customRecord.setInstanceID(this.instanceIDs.toString());
  // customRecord.setExperimentTaskID(UUID.randomUUID());

  // this.reportFn.apply(customRecord);
  // }

  /**
   * Method return calculated problem score for configs score over all instances.
   *
   * @param configID ConfigID.
   * @return ProblemScore
   * @throws Exception Exception.
   */
  // protected double getProblemScore(String configID) throws Exception {
  // List<Double> instanceScores = new ArrayList<>();
  // for (String instanceID : this.instanceIDs) {
  // instanceScores.add(this.configCache.get(configID).get(instanceID).getScore());
  // }

  // return this.problemScoreCalculator.calculateProblemScore(
  // this.instanceIDs.toArray(new String[]{}),
  // instanceScores.stream().mapToDouble(a -> a).toArray());
  // }


  /**
   * Method ranks subjects'.
   *
   * @param subjects Subjects.
   * @param instanceID Instance ID.
   * @return Ranked subjects by objvalue.
   */
  // protected HashMap<String, Integer> getRankedSubjectsObjValue(
  // List<SingleAlgorithmExperimentTaskSubject> subjects,
  // String instanceID) throws Exception {
  // HashMap<String, Integer> rankedSubjects = new HashMap<>();
  // // Get config ids
  // List<String> sortedConfigIDsByObjVal = new ArrayList<>();
  // for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
  // sortedConfigIDsByObjVal.add(subject.getAlgorithmParams().hash());
  // }
  // // Sort config ids
  // Collections.sort(sortedConfigIDsByObjVal, (c1, c2) -> (
  // this.configCache.get(c1).get(instanceID).getObjValue().compareTo(
  // this.configCache.get(c2).get(instanceID).getObjValue())));

  // // set the subjects ranking
  // for (Integer i = 0; i < sortedConfigIDsByObjVal.size(); i++) {
  // String configID = sortedConfigIDsByObjVal.get(i);
  // rankedSubjects.put(configID, i + 1);
  // }

  // // Logg the best config info
  // String bestConfigID = sortedConfigIDsByObjVal.get(sortedConfigIDsByObjVal.size() - 1);
  // logger.info(String.format(
  // "\t- best configuration: %-10.10s, score %.4g",
  // this.configCache.get(bestConfigID).get(instanceID).getConfigID(),
  // this.configCache.get(bestConfigID).get(instanceID).getScore()));
  // return rankedSubjects;
  // }

  @Override
  protected double[] evaluate(SingleAlgorithmExperimentTaskSubject solution) throws Exception {
    throw new UnsupportedOperationException("Should be unimplemented");
  }
}
