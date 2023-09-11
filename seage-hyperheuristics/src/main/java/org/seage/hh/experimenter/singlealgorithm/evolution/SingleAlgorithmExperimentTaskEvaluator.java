package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import org.seage.aal.algorithm.AlgorithmParams;
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

  private int stageId;
  private int jobId;

  private HashMap<String, HashMap<String, Double>> configCache;

  /**
   * Constructor.
   *
   * @param experimentId Experiment ID.
   * @param problemID Problem ID.
   * @param instanceID Instance ID.
   * @param algorithmID Algorithm ID.
   * @param timeoutS Timeout [s]. 
   */
  public SingleAlgorithmExperimentTaskEvaluator(
      UUID experimentId, String problemID, List<String> instanceIDs,
      String algorithmID, long timeoutS, Function<ExperimentTaskRecord, Void> reportFn) {
    super();
    this.experimentId = experimentId;
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.algorithmID = algorithmID;
    this.timeoutS = timeoutS;
    this.configCache = new HashMap<>();
    for (String instanceID : instanceIDs) {
      this.configCache.put(instanceID, new HashMap<>());
    }
    this.reportFn = reportFn;
    this.stageId = 0;
  }

  /**
   * The method creates the task map from configurations that haven't been used on a given instance.
   *
   * @param subjects Algoritm configurations.
   * @param instanceID Instance id.
   * @return task map.
   */
  private HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> createTaskMap (
      List<SingleAlgorithmExperimentTaskSubject> subjects, String instanceID
  ) throws Exception {
    HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> taskMap = new HashMap<>();
    
    HashMap<String, Double> curConfigCache = this.configCache.get(instanceID);

    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
      for (int i = 0; i < subject.getChromosome().getLength(); i++) {
        algorithmParams.putValue(subject.getParamNames()[i], subject.getChromosome().getGene(i));
      }
      // Calculate the subject hash
      String configId = algorithmParams.hash();

      if (curConfigCache.containsKey(configId)) {
        subject.setObjectiveValue(new double[] { curConfigCache.get(configId) });
      } else {
        ExperimentTaskRequest task = new ExperimentTaskRequest(
            UUID.randomUUID(),
            this.experimentId,
            this.jobId, this.stageId,
            this.problemID,
            instanceID,
            this.algorithmID,
            algorithmParams,
            null,
            this.timeoutS
        );

        taskMap.put(task, subject);
        jobId += 1;
      }
    }
    return taskMap;
  }

  @Override
  public void evaluateSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception {
    // TODO
    this.stageId += 1;
    this.jobId = 1;

    HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> taskMap = 
      createTaskMap(subjects, algorithmID);
    List<ExperimentTaskRequest> taskQueue = new ArrayList<>(taskMap.keySet());

    // Run tasks
    LocalExperimentTasksRunner experimentTasksRunner = new LocalExperimentTasksRunner();
    experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);


    // TODO calculate the weighted order
    
    for (ExperimentTaskRequest task : taskQueue) {
      SingleAlgorithmExperimentTaskSubject subject = taskMap.get(task);
      double value = 0.0;
      try {
        // Getting the best objective task value
        value = this.configCache.get(task.getInstanceID()).get(task.getConfigID());

      } catch (Exception ex) {
        logger.warn("Unable to set value");
      }

      // Setting of fitness
      subject.setObjectiveValue(new double[] {value});
    }
  }

  protected Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      double taskObjValue = experimentTask.getValue();
      
      logger.debug("Report for config id: {}", experimentTask.getConfigID());
      logger.debug("Curr task value: {}", taskObjValue);

      // Put the objective value of experiment into the configcache
      this.configCache.get(experimentTask.getInstanceID())
          .put(experimentTask.getConfigID(), taskObjValue);

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
