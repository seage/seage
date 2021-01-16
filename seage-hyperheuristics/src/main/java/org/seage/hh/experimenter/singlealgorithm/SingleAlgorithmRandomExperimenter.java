package org.seage.hh.experimenter.singlealgorithm;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.Experimenter;
import org.seage.hh.experimenter.config.Configurator;
import org.seage.hh.experimenter.config.RandomConfigurator;

public class SingleAlgorithmRandomExperimenter extends Experimenter {
  protected Configurator _configurator;
  private int _numConfigs;
  private int _timeoutS;

  private final int NUM_RUNS = 3;

  public SingleAlgorithmRandomExperimenter(String problemID, String[] instanceIDs, String[] algorithmIDs,
      int numConfigs, int timeoutS) throws Exception {
    super("SingleAlgorithmRandom", problemID, instanceIDs, algorithmIDs);

    _numConfigs = numConfigs;
    _timeoutS = timeoutS;

    _configurator = new RandomConfigurator();
  }

  @Override
  protected void runExperiment(ProblemInstanceInfo instanceInfo) throws Exception {
    for (int i = 0; i < this.algorithmIDs.length; i++) {
      String algorithmID = this.algorithmIDs[i];
      String instanceID = instanceInfo.getInstanceID();

      _logger.info(String.format("%-15s %-24s (%d/%d)", "Algorithm: ", algorithmID, i + 1, this.algorithmIDs.length));
      _logger.info(String.format("%-44s", "   Running... "));

      List<ExperimentTask> taskQueue = new ArrayList<ExperimentTask>();
      ProblemConfig[] configs = _configurator.prepareConfigs(
          this.problemInfo, instanceInfo.getInstanceID(), algorithmID, _numConfigs);
      for (ProblemConfig config : configs) {
        for (int runID = 1; runID <= NUM_RUNS; runID++) {
          // String reportName = problemInfo.getProblemID() + "-" + algorithmID + "-" +
          // instanceInfo.getInstanceID() + "-" + configID + "-" + runID + ".xml";
          taskQueue.add(new ExperimentTask(this.experimentName, this.experimentID, 
              this.problemID, instanceID, algorithmID,
              config.getAlgorithmParams(), runID, _timeoutS));
        }
      }
      String reportPath = String.format("output/experiment-logs/%s-%s-%s-%s.zip", this.experimentID, this.problemID, instanceID,
          algorithmID);

      this.experimentTasksRunner.performExperimentTasks(taskQueue, reportPath);
    }
  }

  @Override
  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return getNumberOfConfigs(instancesCount, algorithmsCount) * _timeoutS * 1000;
  }

  @Override
  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return _numConfigs * NUM_RUNS * instancesCount * algorithmsCount;
  }
}
