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
import org.seage.hh.experimenter.configurator.RandomConfigurator;

/**
 * Experimenter running producing random configs according to the metadata.
 */
public class SingleAlgorithmRandomExperimenter extends Experimenter {
  protected Configurator configurator;
  private int numConfigs;
  private int timeoutS;

  private static final int NUM_RUNS = 3;

  /**
   * SingleAlgorithmRandomExperimenter constructor - nothing special.
   */
  public SingleAlgorithmRandomExperimenter(String problemID, String[] instanceIDs,
      String[] algorithmIDs, int numConfigs, int timeoutS) throws Exception {
    super("SingleAlgorithmRandom", problemID, instanceIDs, algorithmIDs);

    this.numConfigs = numConfigs;
    this.timeoutS = timeoutS;

    configurator = new RandomConfigurator();
  }

  @Override
  protected void runExperiment(ProblemInstanceInfo instanceInfo) throws Exception {
    for (int i = 0; i < this.algorithmIDs.length; i++) {
      String algorithmID = this.algorithmIDs[i];
      String instanceID = instanceInfo.getInstanceID();

      logger.info(String.format("%-15s %-24s (%d/%d)", "Algorithm: ", algorithmID, i + 1,
          this.algorithmIDs.length));
      logger.info(String.format("%-44s", "   Running... "));

      List<ExperimentTask> taskQueue = new ArrayList<ExperimentTask>();
      ProblemConfig[] configs = configurator.prepareConfigs(this.problemInfo,
          instanceInfo.getInstanceID(), algorithmID, this.numConfigs);
      for (ProblemConfig config : configs) {
        for (int runID = 1; runID <= NUM_RUNS; runID++) {
          // String reportName = problemInfo.getProblemID() + "-" + algorithmID + "-" +
          // instanceInfo.getInstanceID() + "-" + configID + "-" + runID + ".xml";
          // taskQueue.add(new ExperimentTask(this.experimentName, this.experimentID,
          // this.problemID, instanceID, algorithmID,
          // config.getAlgorithmParams(), runID, this.timeoutS));
          taskQueue.add(new ExperimentTask(UUID.randomUUID(), this.experimentID, 1, 1, this.problemID, instanceID,
              algorithmID, config.getAlgorithmParams(), this.timeoutS));
        }
      }
      // String reportPath = String.format("output/experiment-logs/%s-%s-%s-%s.zip",
      // this.experimentID, this.problemID, instanceID, algorithmID);

      // RUN EXPERIMENTS
      List<DataNode> stats =
          this.experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);

      // Update score
      double bestObjVal = Double.MAX_VALUE;
      for (DataNode s : stats) {
        double objVal = s.getValueDouble("bestObjVal");
        if (objVal < bestObjVal) {
          bestObjVal = objVal;
        }
      }
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
    return getNumberOfConfigs(instancesCount, algorithmsCount) * this.timeoutS * 1000;
  }

  @Override
  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return this.numConfigs * NUM_RUNS * instancesCount * algorithmsCount;
  }
}
