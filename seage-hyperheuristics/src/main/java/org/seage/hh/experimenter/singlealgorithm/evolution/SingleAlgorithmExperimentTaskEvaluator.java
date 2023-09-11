package org.seage.hh.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.singlealgorithm.SingleAlgorithmExperiment;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.runner.LocalExperimentTasksRunner;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.thread.Task;
import org.seage.thread.TaskRunner3;
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
  private String instanceID;
  private String algorithmID;
  private long timeoutS;
  private Function<ExperimentTaskRecord, Void> reportFn;

  private int stageId;

  private HashMap<String, Double> configCache;

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
      UUID experimentId, String problemID, String instanceID,
      String algorithmID, long timeoutS, Function<ExperimentTaskRecord, Void> reportFn) {
    super();
    this.experimentId = experimentId;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.timeoutS = timeoutS;
    this.configCache = new HashMap<>();
    this.reportFn = reportFn;
    this.stageId = 0;
  }

  @Override
  public void evaluateSubjects(
      List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception {
    List<ExperimentTaskRequest> taskQueue = new ArrayList<>();
    HashMap<ExperimentTaskRequest, SingleAlgorithmExperimentTaskSubject> taskMap = new HashMap<>();
    
    this.stageId += 1;

    int jobId = 1;
    for (SingleAlgorithmExperimentTaskSubject subject : subjects) {
      AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
      for (int i = 0; i < subject.getChromosome().getLength(); i++) {
        algorithmParams.putValue(subject.getParamNames()[i], subject.getChromosome().getGene(i));
      }

      String configId = algorithmParams.hash();
      if (this.configCache.containsKey(configId)) {
        subject.setObjectiveValue(new double[] { this.configCache.get(configId) });
      } else {
        ExperimentTaskRequest task = new ExperimentTaskRequest(
            UUID.randomUUID(),
            this.experimentId,
            jobId, this.stageId,
            this.problemID,
            this.instanceID,
            this.algorithmID,
            algorithmParams,
            null,
            this.timeoutS
        );

        taskMap.put(task, subject);
        taskQueue.add(task);
        jobId += 1;
      }
    }

    // Todo
    // LocalExperimentTasksRunner experimentTasksRunner = new LocalExperimentTasksRunner();

    // experimentTasksRunner.performExperimentTasks(taskQueue, null);

    LocalExperimentTasksRunner experimentTasksRunner = new LocalExperimentTasksRunner();
    experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);


    // Outdated - still using datanode
    // TaskRunner3.run(
    //     taskQueue.toArray(new Task[] {}), Runtime.getRuntime().availableProcessors());

    for (ExperimentTaskRequest task : taskQueue) {
      SingleAlgorithmExperimentTaskSubject subject = taskMap.get(task);
      double value = 0.0;
      try {
        // Getting the best objective task value
        value = this.configCache.get(task.getConfigID());

        // Non-existent method to get bestObjValue!
        // value = task.getExperimentTaskReport()
        //             .getDataNode("AlgorithmReport").getDataNode("Statistics")
        //             .getValueDouble("bestObjVal");
      } catch (Exception ex) {
        logger.warn("Unable to set value");
      }

      // Setting of fitness
      subject.setObjectiveValue(new double[] {value});
    }
  }

  protected Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      double taskScore = experimentTask.getScore();
      logger.debug("Report for config id: {}", experimentTask.getConfigID());
      logger.info("Curr task value: {}", taskScore);
      // Put the value of experiment into the configcache
      // TODO - is it ok to use score insted of value?
      this.configCache.put(experimentTask.getConfigID(), taskScore);

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
