package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
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

  private int stageId;

  private HashMap<String, HashMap<String, ExperimentTaskRecord>> configCache;
  private HashMap<Integer, String> subjectHashToConfigIDMap;

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
    // for (String instanceID : instanceIDs) {
    //   this.configCache.put(instanceID, new HashMap<>());
    // }
    this.instancesInfo = instancesInfo;
    this.reportFn = reportFn;
    this.stageId = 0;

    // Sort the instancesIds based on their size 
    Comparator<String> comparator = (i1, i2) -> {
      try {
        return Double.compare(
            this.instancesInfo.get(i1).getValueDouble("size"), 
            this.instancesInfo.get(i2).getValueDouble("size"));
      } catch (Exception e) {
        return 0;
      }
    };
    Collections.sort(this.instanceIDs, comparator);
  }


  /**
   * The method creates the task map from configurations that haven't been used on a given instance.
   *
   * @param subjects Algoritm configurations.
   * @param instanceID Instance id.
   * @return task map.
   */
  private List<ExperimentTaskRequest> createTaskList(
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
   */ // TODO - not evaluate, but objective value for row is being calculated here
  private void setSubjectsConfigScore(
      List<SingleAlgorithmExperimentTaskSubject> subjects,
      HashMap<String, HashMap<String, Integer>> rankedSubjects) throws Exception {
    
    // Each subject evaluate separatly
    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      String configID = this.subjectHashToConfigIDMap.get(subject.hashCode());
    
      double sumOfWeights = 0.0;
      double result = 0.0;
      for (String instanceID : this.instanceIDs) {
        double instanceSize = this.instancesInfo.get(instanceID).getValueDouble("size");
        double instanceRank = rankedSubjects.get(configID).get(instanceID);

        result += instanceSize * instanceRank;
        sumOfWeights += instanceSize;
      }
      if (sumOfWeights == 0.0) {
        throw new Exception("Error: dividing by zero.");
      }
      // Config score
      Double configScore = result / sumOfWeights;
      subject.setObjectiveValue(new double[] {configScore});
    }
  }

  /**
   * Method reports the experiment config score.
   *
   * @param subject Config subject.
   * @param configScore Config score.
   * @throws Exception Exception.
   */
  private void reportSubjectsProblemScore(
      List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception{
    
    ProblemInfo problemInfo = ProblemProvider
          .getProblemProviders()
          .get(this.problemID)
          .getProblemInfo();
      
    ProblemScoreCalculator problemScoreCalculator = new ProblemScoreCalculator(problemInfo);

    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
      for (int i = 0; i < subject.getChromosome().getLength(); i++) {
        algorithmParams.putValue(subject.getParamNames()[i], subject.getChromosome().getGene(i));
      }
      String configID = algorithmParams.hash();

      List<Double> instanceScores = new ArrayList<>();
      for (String instanceID : this.instanceIDs) {
        instanceScores.add(this.configCache.get(configID).get(instanceID).getScore());
      }

      double problemScore = problemScoreCalculator.calculateProblemScore(
            this.instanceIDs.toArray(new String[]{}), 
            instanceScores.stream().mapToDouble(a -> a).toArray());

      ExperimentTaskRequest customRequest = new ExperimentTaskRequest(
          this.experimentId, 
          this.experimentId, 
          1, 
          stageId, 
          this.problemID, 
          "ALL", 
          this.algorithmID, 
          configID, 
          algorithmParams, 
          null, 
          this.timeoutS
      );
      ExperimentTaskRecord customRecord = new ExperimentTaskRecord(customRequest);
      customRecord.setScore(problemScore);

      this.reportFn.apply(customRecord);
    }
  }


  /**
   * Method ranks subjects'.
   *
   * @param instanceID Instance ID.
   * @param rankedSubjects Map to store the ranks.
   */
  private void rankSubjectsObjValue(
    String instanceID, HashMap<String, HashMap<String, Integer>> rankedSubjects) {
    // Sort subjects
    List<String> sortedConfigIDsByObjVal = new ArrayList<>(this.configCache.keySet());

    Comparator<String> comparator = (c1, c2) -> this.configCache.get(c1)
        .get(instanceID).getObjValue().compareTo(
          this.configCache.get(c2).get(instanceID).getObjValue());
    Collections.sort(sortedConfigIDsByObjVal, comparator.reversed());

    // set the subjects ranking 
    for (Integer i = 0; i < sortedConfigIDsByObjVal.size(); i++) {
      String configID = sortedConfigIDsByObjVal.get(i);
      if (!rankedSubjects.containsKey(configID)) {
        rankedSubjects.put(configID, new HashMap<>());
      }
      rankedSubjects.get(configID).put(instanceID, i + 1);
    }
  }

  @Override
  public void evaluateSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects
  ) throws Exception {
    this.stageId += 1;
    HashMap<String, HashMap<String, Integer>> rankedSubjects = new HashMap<>();

    // Create and run tasks for each columns separatly
    for (int i = 0; i < this.instanceIDs.size(); i++) {
      String instanceID = this.instanceIDs.get(i);
      logger.info("Iteration \t ({}/{})", i + 1, this.instanceIDs.size());
      List<ExperimentTaskRequest> taskIDs = createTaskList(subjects, instanceID);
      
      // Run tasks
      LocalExperimentTasksRunner experimentTasksRunner = new LocalExperimentTasksRunner();
      experimentTasksRunner.performExperimentTasks(taskIDs, this::reportExperimentTask);

      // Rank subjects
      this.rankSubjectsObjValue(instanceID, rankedSubjects);
    }
    // Set the configuration objective value
    this.setSubjectsConfigScore(subjects, rankedSubjects);
    this.reportSubjectsProblemScore(subjects);
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

  @Override
  protected double[] evaluate(SingleAlgorithmExperimentTaskSubject solution) throws Exception {
    throw new UnsupportedOperationException("Should be unimplemented");
  }
}
