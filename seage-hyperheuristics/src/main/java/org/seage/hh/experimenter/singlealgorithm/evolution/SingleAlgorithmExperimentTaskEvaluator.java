package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.problem.ProblemInstanceInfo;
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

  private HashMap<String, HashMap<String, Double>> configCache;
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
      HashMap<String, Double> curConfigCache = this.configCache.get(configId);
  
      this.subjectHashToConfigIDMap.put(subject.hashCode(), configId);
  
      // TODO - solve the cache, instanceID and configID needed
      if (curConfigCache.containsKey(instanceID)) {
        subject.setObjectiveValue(new double[] { curConfigCache.get(instanceID) });
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
  private void setSubjectsObjectiveValue(
      List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception {

    // Each subject evaluate separatly
    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      String configID = this.subjectHashToConfigIDMap.get(subject.hashCode());

      // Get the table row (results of config over instances)
      Map<String, Double> instancesObjVal = this.configCache.get(configID);

      List<String> sortedInstancesByObjVal = new ArrayList<>(instancesObjVal.keySet());

      // Sort by the objective value of the task on specific instance
      Comparator<String> comparator = (i1, i2) -> instancesObjVal
          .get(i1).compareTo(instancesObjVal.get(i2));
      Collections.sort(sortedInstancesByObjVal, comparator.reversed());
    
      double sumOfWeights = 0.0;
      double result = 0.0;
      for (String instanceID : this.instanceIDs) {
        double instanceSize = this.instancesInfo.get(instanceID).getValueDouble("size");
        double instanceRank = sortedInstancesByObjVal.indexOf(instanceID) + 1.0;

        result += instanceSize * instanceRank;
        sumOfWeights += instanceSize;
      }
      if (sumOfWeights == 0.0) {
        throw new Exception("Error: dividing by zero.");
      }
      subject.setObjectiveValue(new double[] {result / sumOfWeights});
    }
  }


  @Override
  public void evaluateSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects
  ) throws Exception {
    this.stageId += 1;

    // Create and run tasks for each columns separatly
    for (int i = 0; i < this.instanceIDs.size(); i++) {
      String instanceID = this.instanceIDs.get(i);
      logger.info("Iteration \t ({}/{})", i + 1, this.instanceIDs.size());
      List<ExperimentTaskRequest> taskIDs = createTaskList(subjects, instanceID);
      
      // Run tasks
      LocalExperimentTasksRunner experimentTasksRunner = new LocalExperimentTasksRunner();
      experimentTasksRunner.performExperimentTasks(taskIDs, this::reportExperimentTask);
    }
    // Set the configuration objective value
    // logger.debug("setSubjectsObjectiveValue starts");
    this.setSubjectsObjectiveValue(subjects);
  }

  protected Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      double taskObjValue = experimentTask.getValue();
      
      logger.info("Task score: {}, value: {}", experimentTask.getScore(), taskObjValue);

      // Put the objective value of experiment into the configcache
      // TODO - config cache is representing the table - instanceID + configID
      this.configCache.get(experimentTask.getConfigID())
          .put(experimentTask.getInstanceID(), taskObjValue);

      // Run the SingleAlgorithmEvolutionExperiment reporter
      this.reportFn.apply(experimentTask);
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
