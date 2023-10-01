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
  private ProblemInfo problemInfo;
  private ProblemScoreCalculator problemScoreCalculator;

  protected int stageId;
  protected HashMap<String, HashMap<String, ExperimentTaskRecord>> configCache;
  protected HashMap<Integer, String> subjectHashToConfigIDMap;

  /**
   * Constructor.
   *
   * @param experimentId Experiment ID.
   * @param problemID Problem ID.
   * @param instanceIDs Instance IDs.
   * @param algorithmID Algorithm ID.
   * @param timeoutS Timeout [s]. 
   */
  public SingleAlgorithmExperimentTaskEvaluator(
      UUID experimentId, String problemID, List<String> instanceIDs,
      String algorithmID, long timeoutS, 
      ProblemInfo problemInfo,
      Map<String, ProblemInstanceInfo> instancesInfo, 
      Function<ExperimentTaskRecord, Void> reportFn) {
    super();
    this.experimentId = experimentId;
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.algorithmID = algorithmID;
    this.timeoutS = timeoutS;
    this.configCache = new HashMap<>();
    this.subjectHashToConfigIDMap = new HashMap<>();
    this.instancesInfo = instancesInfo;
    this.problemInfo = problemInfo;
    this.reportFn = reportFn;
    this.stageId = 0;
    this.problemScoreCalculator = new ProblemScoreCalculator(problemInfo);
  }

  @Override
  public void evaluateSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects
  ) throws Exception {
    this.stageId += 1;
    HashMap<String, HashMap<String, Integer>> rankedSubjects = new HashMap<>();

    // Create and run tasks for each columns separatly
    for (String instanceID : this.instanceIDs) {
      List<ExperimentTaskRequest> taskRequests = createTaskList(subjects, instanceID);
      
      // Run tasks
      logger.info("Evaluating {} configs for instance {}", taskRequests.size(), instanceID);
      LocalExperimentTasksRunner experimentTasksRunner = new LocalExperimentTasksRunner();
      experimentTasksRunner.performExperimentTasks(taskRequests, this::reportExperimentTask);

      // Rank subjects
      rankedSubjects.put(instanceID, 
          this.getRankedSubjectsObjValue(subjects, instanceID));
    }
    // Set the configuration objective value
    SingleAlgorithmExperimentTaskSubject bestConfig = 
        this.setConfigScoreToSubjects(subjects, rankedSubjects);
  
    this.reportSubjectsProblemScore(bestConfig);
  }

  protected Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      // Put the objective value of experiment into the configcache
      this.configCache.get(experimentTask.getConfigID())
          .put(experimentTask.getInstanceID(), experimentTask);

    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }

  /**
   * The method creates the task map from configurations that haven't been used on a given instance.
   *
   * @param subjects Algoritm configurations.
   * @param instanceID Instance id.
   * @return task map.
   */
  protected List<ExperimentTaskRequest> createTaskList(
      List<SingleAlgorithmExperimentTaskSubject> subjects, String instanceID
  ) throws Exception {
    List<ExperimentTaskRequest> taskList = new ArrayList<>();

    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
      for (int i = 0; i < subject.getChromosome().getLength(); i++) {
        algorithmParams.putValue(subject.getParamNames()[i], subject.getChromosome().getGene(i));
      }
      // Calculate the subject hash
      String configId = algorithmParams.hash();

      // If config hasn't been used before, log it into configCache
      if (!this.configCache.containsKey(configId)) {
        this.configCache.put(configId, new HashMap<>());
      }
      HashMap<String, ExperimentTaskRecord> curConfigCache = this.configCache.get(configId);
  
      this.subjectHashToConfigIDMap.put(subject.hashCode(), configId);
  
      if (curConfigCache.containsKey(instanceID)) {
        subject.setObjectiveValue(new double[] { curConfigCache.get(instanceID).getObjValue() });
      } else {
        ExperimentTaskRequest task = new ExperimentTaskRequest(
            UUID.randomUUID(),
            this.experimentId,
            1, this.stageId,
            this.problemID,
            instanceID,
            this.algorithmID,
            configId,
            algorithmParams,
            null,
            this.timeoutS
        );
        taskList.add(task);
      }
    }
    return taskList;
  }

  /**
   * Method evaluates given subject.
   *
   * @param taskIDs Task queue.
   * @param taskMap Task map.
   * @return Weighted order.
   */
  protected SingleAlgorithmExperimentTaskSubject setConfigScoreToSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects,
      HashMap<String, HashMap<String, Integer>> rankedSubjects) throws Exception {
    
    double bestConfigScore = 0.0;
    SingleAlgorithmExperimentTaskSubject bestConfig = null;
    // Each subject evaluate separatly
    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      String configID = this.subjectHashToConfigIDMap.get(subject.hashCode());
    
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
      Double configScore = result / sumOfWeights;
      subject.setObjectiveValue(new double[] {configScore});

      if (bestConfigScore <= configScore) {
        bestConfig = subject;
        bestConfigScore = configScore;
      }
    }
    
    // Returns the best config id
    return bestConfig;
  }

  /**
   * Method reports the experiment config score.
   *
   * @param subject Config subject.
   * @param configScore Config score.
   * @throws Exception Exception.
   */
  protected void reportSubjectsProblemScore(
      SingleAlgorithmExperimentTaskSubject bestConfig) throws Exception {
    String bestConfigID = this.subjectHashToConfigIDMap.get(bestConfig.hashCode());
    double problemScore = getProblemScore(bestConfigID);

    logger.info(String.format("Best overall configuration %-10.10s confScore %.4g score %.4g", 
          bestConfigID, bestConfig.getObjectiveValue(), problemScore));

    ExperimentTaskRecord customRecord = 
        this.configCache.get(bestConfigID).get(this.instanceIDs.get(0));

    customRecord.setScore(problemScore);
    customRecord.setInstanceID(this.instanceIDs.toString());

    this.reportFn.apply(customRecord);
  }

  /**
   * Method return calculated problem score for configs score over all instances.
   *
   * @param configID ConfigID.
   * @return ProblemScore
   * @throws Exception Exception.
   */
  protected double getProblemScore(String configID) throws Exception {
    List<Double> instanceScores = new ArrayList<>();
    for (String instanceID : this.instanceIDs) {
      instanceScores.add(this.configCache.get(configID).get(instanceID).getScore());
    }

    return this.problemScoreCalculator.calculateProblemScore(
        this.instanceIDs.toArray(new String[]{}), 
        instanceScores.stream().mapToDouble(a -> a).toArray());
  }


  /**
   * Method ranks subjects'.
   *
   * @param instanceID Instance ID.
   * @param rankedSubjects Map to store the ranks.
   */
  protected HashMap<String, Integer> getRankedSubjectsObjValue(
      List<SingleAlgorithmExperimentTaskSubject> subjects,
      String instanceID) {
    HashMap<String, Integer> rankedSubjects = new HashMap<>();
    // Get config ids
    List<String> sortedConfigIDsByObjVal = new ArrayList<>();
    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      sortedConfigIDsByObjVal.add(this.subjectHashToConfigIDMap.get(subject.hashCode()));
    }
    // Sort config ids
    Collections.sort(sortedConfigIDsByObjVal, (c1, c2) -> (
        this.configCache.get(c1).get(instanceID).getObjValue().compareTo(
        this.configCache.get(c2).get(instanceID).getObjValue())));

    // set the subjects ranking 
    for (Integer i = 0; i < sortedConfigIDsByObjVal.size(); i++) {
      String configID = sortedConfigIDsByObjVal.get(i);
      rankedSubjects.put(configID, i + 1);
    }

    // Logg the best config info
    String bestConfigID = sortedConfigIDsByObjVal.get(sortedConfigIDsByObjVal.size() - 1);
    logger.info(String.format(
        "\t- best configuration: %-10.10s, score %.4g", 
        this.configCache.get(bestConfigID).get(instanceID).getConfigID(),
        this.configCache.get(bestConfigID).get(instanceID).getScore()));
    return rankedSubjects;
  }

  @Override
  protected double[] evaluate(SingleAlgorithmExperimentTaskSubject solution) throws Exception {
    throw new UnsupportedOperationException("Should be unimplemented");
  }
}
