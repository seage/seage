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
    for (String instanceID : instanceIDs) {
      this.configCache.put(instanceID, new HashMap<>());
    }
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
  private HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> createTaskMap(
      SingleAlgorithmExperimentTaskSubject subject, List<String> instanceIDs
  ) throws Exception {
    HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> taskMap = new HashMap<>();

    AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
    for (int i = 0; i < subject.getChromosome().getLength(); i++) {
      algorithmParams.putValue(subject.getParamNames()[i], subject.getChromosome().getGene(i));
    }

    for (String instanceID : instanceIDs) {
      HashMap<String, Double> curConfigCache = this.configCache.get(instanceID);

      // Calculate the subject hash
      String configId = algorithmParams.hash();

      if (curConfigCache.containsKey(configId)) {
        subject.setObjectiveValue(new double[] { curConfigCache.get(configId) });
      } else {
        ExperimentTaskRequest task = new ExperimentTaskRequest(
            UUID.randomUUID(),
            this.experimentId,
            1, this.stageId,
            this.problemID,
            instanceID,
            this.algorithmID,
            algorithmParams,
            null,
            this.timeoutS
        );

        taskMap.put(task, subject);
      }
    }
    return taskMap;
  }

  /**
   * Method evaluates given subject.
   *
   * @param taskQueue Task queue.
   * @param taskMap Task map.
   * @return Weighted order.
   */
  private double evaluateSubject(
      List<ExperimentTaskRequest> taskQueue) throws Exception {

    // Sort the task queue based on the bestObjValue
    Comparator<ExperimentTaskRequest> comparator = (t1, t2) -> this.configCache.get(
        t1.getInstanceID()).get(t1.getConfigID()).compareTo(this.configCache.get(
        t1.getInstanceID()).get(t2.getConfigID()));
    Collections.sort(taskQueue, comparator.reversed());
    
    double result = 0;
    double sumOfWeights = 0;
    int taskPos = 1;
    for (ExperimentTaskRequest task : taskQueue) {
      double weight = Math.pow(10, this.instanceIDs.indexOf(task.getInstanceID()));
      sumOfWeights += weight;
      result += taskPos * weight;
      taskPos += 1;
    }

    if (sumOfWeights == 0) {
      throw new Exception("Sum of weights result in zero.");
    }

    return result / sumOfWeights;
  }

  @Override
  public void evaluateSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects
  ) throws Exception {
    this.stageId += 1;

    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> taskMap = 
          createTaskMap(subject, instanceIDs);
      List<ExperimentTaskRequest> taskQueue = new ArrayList<>(taskMap.keySet());

      // Run tasks
      LocalExperimentTasksRunner experimentTasksRunner = new LocalExperimentTasksRunner();
      experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);

      // Set the configuration objective value
      subject.setObjectiveValue(new double[] {evaluateSubject(taskQueue)});
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
