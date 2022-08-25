package org.seage.hh.experimenter.singlealgorithm;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.configurator.Configurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;

/**
 * Experimenter running producing random configs according to the metadata.
 */
public class SingleAlgorithmExperimenter extends Experimenter {
  protected Configurator configurator;
  private int numConfigs;
  protected List<String> algorithmIDs;
  protected IExperimentTasksRunner experimentTasksRunner;

  private static final int NUM_RUNS = 3;

  /**
   * SingleAlgorithmExperimenter constructor - nothing special.
   */
  protected SingleAlgorithmExperimenter(
      List<String> algorithmIDs,
      Map<String, List<String>> instanceIDsPerProblems,
      int numConfigs, 
      int numRuns,
      int timeoutS
  ) throws Exception {
    super("", instanceIDsPerProblems, numRuns, timeoutS);
    this.algorithmIDs = algorithmIDs;
    this.numConfigs = numConfigs;
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
  }
  
  @Override
  public void runExperiment() throws Exception {
    // Run experiment tasks for each instance
    for (Entry<String, List<String>> entry : instanceIDsPerProblems.entrySet()) {
      String problemID = entry.getKey();
      
      ProblemInfo problemInfo = ProblemProvider
          .getProblemProviders()
          .get(problemID)
          .getProblemInfo();

      int instanceIndex = 0;
      for (String instanceID : entry.getValue()) {
        try {
          logger.info("-------------------------------------");
          logger.info(String.format("%-15s %s", "Problem:", problemID));
          logger.info(String.format("%-15s %-16s    (%d/%d)", "Instance:", 
              instanceID, instanceIndex + 1, entry.getValue().size()));
          ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(
              instanceID);
          runExperimentTasksForProblemInstance(instanceInfo);
        } catch (Exception ex) {
          logger.warn(ex.getMessage(), ex);
        }
        instanceIndex++;
      }
    }
  }

  protected void runExperimentTasksForProblemInstance(
      ProblemInstanceInfo instanceInfo) throws Exception {

    for (Entry<String, List<String>> entry : instanceIDsPerProblems.entrySet()) {
      String problemID = entry.getKey();

      ProblemInfo problemInfo = ProblemProvider
          .getProblemProviders()
          .get(problemID)
          .getProblemInfo();
      
      for (int i = 0; i < this.algorithmIDs.size(); i++) {
        String algorithmID = this.algorithmIDs.get(i);
        String instanceID = instanceInfo.getInstanceID();
  
        logger.info(String.format("%-15s %-24s (%d/%d)", "Algorithm: ", 
            algorithmID, i + 1, this.algorithmIDs.size()));
        logger.info(String.format("%-44s", "   Running... "));
  
        // The taskQueue size must be limited since the results are stored in the task's reports
        // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
  
        List<ExperimentTaskRequest> taskQueue = new ArrayList<>();
        // Prepare experiment task configs
        ProblemConfig[] configs = configurator.prepareConfigs(problemInfo,
            instanceInfo.getInstanceID(), algorithmID, numConfigs);
  
        // Enqueue experiment tasks
        for (ProblemConfig config : configs) {
          for (int runID = 1; runID <= NUM_RUNS; runID++) {
            taskQueue.add(new ExperimentTaskRequest(
                UUID.randomUUID(), this.experimentID, runID, 1, problemID, instanceID,
                algorithmID, config.getAlgorithmParams(), null, this.timeoutS));
          }
        }
  
        // RUN EXPERIMENT TASKS
        this.experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);
      }
    }
  }

  private Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
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

  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return (long)Math.ceil((double)getNumberOfConfigs(instancesCount, algorithmsCount) 
        / Runtime.getRuntime().availableProcessors())
        * this.timeoutS * 1000;
  }

  
  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long)this.numConfigs * NUM_RUNS * instancesCount * algorithmsCount;
  }
}
