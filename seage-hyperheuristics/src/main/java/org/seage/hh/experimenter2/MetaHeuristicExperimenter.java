package org.seage.hh.experimenter2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaHeuristicExperimenter implements AlgorithmExperimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());
  protected DefaultConfigurator configurator;
  protected ProblemInfo problemInfo;
  protected ExperimentReporter experimentReporter;
  protected IExperimentTasksRunner experimentTasksRunner;

  private static final int NUM_RUNS = 3;
  
  protected UUID experimentID;
  protected String problemID;
  protected String instanceID;
  protected String algorithmID;
  protected int numConfigs;
  protected int timeoutS;

  /**
   * MetaHeuristicExperimenter constructor.
   */
  protected MetaHeuristicExperimenter(
      UUID experimentID, String problemID, String instanceID, 
      String algorithmID, int numConfigs, int timeoutS) {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.numConfigs = numConfigs;
    this.timeoutS = timeoutS;
  }

  /**
   * .
   */
  public void runExperiment() {
    logger.info("Running MetaheuristicExperimenter");
    try {
      experimentMain();
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
  }

  protected void experimentMain() throws Exception {
    try {
      logger.info("-------------------------------------");
      logger.info(String.format("%-15s %s", "Problem:", problemID));
      logger.info(String.format("Instance: " + instanceID));
      ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
      runExperimentTasksForProblemInstance(instanceInfo);
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
  }


  protected void runExperimentTasksForProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception {
    
    logger.info(String.format("%-44s", "   Running... "));

    double bestObjVal = Double.MAX_VALUE;
    // The taskQueue size must be limited since the results are stored in the task's reports
    // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
    int batchSize = Math.min(numConfigs, Runtime.getRuntime().availableProcessors());
    int batchCount = (int)Math.ceil((double)numConfigs / batchSize);
    for (int j = 0; j < batchCount; j++) {
      if ((j + 1) * batchSize > numConfigs) {
        batchSize = numConfigs % batchSize;
      }
      List<ExperimentTask> taskQueue = new ArrayList<>();
      // Prepare experiment task configs
      ProblemConfig[] configs = configurator.prepareConfigs(problemInfo,
          instanceInfo.getInstanceID(), algorithmID, batchSize);

      // Enqueue experiment tasks
      for (ProblemConfig config : configs) {
        for (int runID = 1; runID <= NUM_RUNS; runID++) {
          taskQueue.add(new ExperimentTask(
              UUID.randomUUID(), experimentID, runID, 1, problemID, instanceID,
              algorithmID, config.getAlgorithmParams(), timeoutS));
        }
      }

      // RUN EXPERIMENT TASKS
      List<DataNode> stats =
          experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);

      // Update score        
      for (DataNode s : stats) {
        double objVal = s.getValueDouble("bestObjVal");
        if (objVal < bestObjVal) {
          bestObjVal = objVal;
        }
      }
    }
    // This is weird - if multiple instances run during the expriment the last best value is written
    experimentReporter.updateScore(experimentID, bestObjVal);
  }

  private Void reportExperimentTask(ExperimentTask experimentTask) {
    try {
      experimentReporter.reportExperimentTask(experimentTask);
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }
}
