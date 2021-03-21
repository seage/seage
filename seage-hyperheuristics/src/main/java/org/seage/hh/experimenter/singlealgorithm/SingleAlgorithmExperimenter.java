package org.seage.hh.experimenter.singlealgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.configurator.Configurator;

/**
 * Experimenter running producing random configs according to the metadata.
 */
public class SingleAlgorithmExperimenter extends Experimenter {
  protected Configurator configurator;
  private int numConfigs;
  private int timeoutS;

  private static final int NUM_RUNS = 3;

  /**
   * SingleAlgorithmExperimenter constructor - nothing special.
   */
  protected SingleAlgorithmExperimenter(String experimentName, String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS) throws Exception {
    super(experimentName, problemID, instanceIDs, algorithmIDs);

    this.numConfigs = numConfigs;
    this.timeoutS = timeoutS;
  }
  
  protected void experimentMain() throws Exception {
    // Run experiment tasks for each instance
    for (int i = 0; i < this.instanceIDs.length; i++) {
      try {
        logger.info("-------------------------------------");
        logger.info(String.format("%-15s %s", "Problem:", this.problemID));
        logger.info(String.format("%-15s %-16s    (%d/%d)", "Instance:", 
            this.instanceIDs[i], i + 1, this.instanceIDs.length));
        ProblemInstanceInfo instanceInfo = this.problemInfo.getProblemInstanceInfo(
            this.instanceIDs[i]);
        runExperimentTasksForProblemInstance(instanceInfo);
      } catch (Exception ex) {
        logger.warn(ex.getMessage(), ex);
      }
    }
  }

  protected void runExperimentTasksForProblemInstance(ProblemInstanceInfo instanceInfo) throws Exception {
    for (int i = 0; i < this.algorithmIDs.length; i++) {
      String algorithmID = this.algorithmIDs[i];
      String instanceID = instanceInfo.getInstanceID();

      logger.info(String.format("%-15s %-24s (%d/%d)", "Algorithm: ", 
          algorithmID, i + 1, this.algorithmIDs.length));
      logger.info(String.format("%-44s", "   Running... "));

      double bestObjVal = Double.MAX_VALUE;
      // The taskQueue size must be limited since the results are stored in the task's reports
      // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
      int batchSize = Math.min(this.numConfigs, Runtime.getRuntime().availableProcessors());
      int batchCount = (int)Math.ceil((double)this.numConfigs / batchSize);
      for (int j = 0; j < batchCount; j++) {
        if ((j + 1) * batchSize > this.numConfigs) {
          batchSize = this.numConfigs % batchSize;
        }
        List<ExperimentTask> taskQueue = new ArrayList<>();
        // Prepare experiment task configs
        ProblemConfig[] configs = configurator.prepareConfigs(this.problemInfo,
            instanceInfo.getInstanceID(), algorithmID, batchSize);

        // Enqueue experiment tasks
        for (ProblemConfig config : configs) {
          for (int runID = 1; runID <= NUM_RUNS; runID++) {
            taskQueue.add(new ExperimentTask(UUID.randomUUID(), this.experimentID, runID, 1, this.problemID, instanceID,
                algorithmID, config.getAlgorithmParams(), this.timeoutS));
          }
        }

        // RUN EXPERIMENT TASKS
        List<DataNode> stats =
            this.experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);

        // Update score        
        for (DataNode s : stats) {
          double objVal = s.getValueDouble("bestObjVal");
          if (objVal < bestObjVal) {
            bestObjVal = objVal;
          }
        }
      }
      // This is weird - if multiple instances run during the expriment the last best value is written
      this.experimentReporter.updateScore(this.experimentID, bestObjVal);
    }
  }

  private Void reportExperimentTask(ExperimentTask experimentTask) {
    try {
      this.experimentReporter.reportExperimentTask(experimentTask);
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }

  @Override
  protected String getExperimentConfig() {
    DataNode config = new DataNode("Config");
    config.putValue("timeoutS", this.timeoutS);
    config.putValue("numConfigs", this.numConfigs);
    
    return config.toString();
  }

  @Override
  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return (long)Math.ceil((double)getNumberOfConfigs(instancesCount, algorithmsCount) 
        / Runtime.getRuntime().availableProcessors())
        * this.timeoutS * 1000;
  }

  @Override
  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long)this.numConfigs * NUM_RUNS * instancesCount * algorithmsCount;
  }
}
