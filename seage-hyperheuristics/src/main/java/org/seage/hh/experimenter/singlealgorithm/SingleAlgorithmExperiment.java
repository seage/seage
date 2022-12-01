package org.seage.hh.experimenter.singlealgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.hh.experimenter.Experiment;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.Configurator;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.seage.hh.runner.IExperimentTasksRunner;
import org.seage.hh.runner.LocalExperimentTasksRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Experimenter running producing random configs according to the metadata.
 */
public class SingleAlgorithmExperiment implements Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(SingleAlgorithmExperiment.class.getName());
  protected Configurator configurator;

  protected IExperimentTasksRunner experimentTasksRunner;
  protected ExperimentReporter experimentReporter;
  protected ProblemInfo problemInfo;
  
  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected String instanceID;
  protected String algorithmID;
  protected int numRuns;
  protected int timeoutS;
  private double bestScore;

  /**
   * SingleAlgorithmExperiment constructor - nothing special.
   */
  protected SingleAlgorithmExperiment(
      UUID experimentID, String problemID, String algorithmID,
      String instanceID, int numRuns, int timeoutS,
      ExperimentReporter experimentReporter
  ) throws Exception {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.algorithmID = algorithmID;
    this.instanceID = instanceID;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    this.experimentReporter = experimentReporter;
    experimentName = "SingleAlgorithm";
    bestScore = 0.0;

    // Initialize
    problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
    configurator = new DefaultConfigurator(0.26);
  }
  
  @Override
  public Double run() throws Exception {
    ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);

    // The taskQueue size must be limited since the results are stored in the task's reports
    // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
    List<ExperimentTaskRequest> taskQueue = new ArrayList<>();

    // Prepare experiment task configs
    ProblemConfig config = configurator.prepareConfigs(problemInfo,
        instanceInfo.getInstanceID(), algorithmID, 2)[1]; // the second with a bit of randomness
      
    // Enqueue experiment tasks
    for (int runID = 1; runID <= numRuns; runID++) {
      taskQueue.add(new ExperimentTaskRequest(
          UUID.randomUUID(), experimentID, runID, 1, problemID, instanceID,
          algorithmID, config.getAlgorithmParams(), null, timeoutS));
    }
    
    // RUN EXPERIMENT TASKS
    experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);

    return bestScore;
  }

  private Void reportExperimentTask(ExperimentTaskRecord experimentTask) {
    try {
      experimentReporter.reportExperimentTask(experimentTask);
      double taskScore = experimentTask.getScore();
      if (taskScore > bestScore) {
        bestScore = taskScore;
      }
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }
}
