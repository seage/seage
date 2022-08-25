package org.seage.hh.experimenter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.seage.hh.experimenter.configurator.ExtendedDefaultConfigurator;
import org.seage.hh.experimenter.configurator.GridConfigurator;
import org.seage.hh.experimenter.configurator.RandomConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
import org.seage.hh.knowledgebase.db.dbo.ExperimentTaskRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaHeuristicExperiment implements Experiment {
  private static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperiment.class.getName());
  private DefaultConfigurator defaultConfigurator;
  private ExtendedDefaultConfigurator feedbackConfigurator;
  private GridConfigurator gridConfigurator;
  private RandomConfigurator randomConfigurator;
  private ProblemInfo problemInfo;
  private IExperimentTasksRunner experimentTasksRunner;
  private ExperimentReporter experimentReporter;
  
  private UUID experimentID;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private int numRuns;
  private int timeoutS;
  private double bestScore;

  /**
   * MetaHeuristicExperimenter constructor.
   */
  protected MetaHeuristicExperiment(
      UUID experimentID, String problemID, String instanceID, 
      String algorithmID, int numRuns, int timeoutS,
      ExperimentReporter experimentReporter) 
      throws Exception {
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
    this.experimentReporter = experimentReporter;
    this.bestScore = 0.0;

    // Initialize all
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    this.feedbackConfigurator = new ExtendedDefaultConfigurator();//
    this.defaultConfigurator = new DefaultConfigurator(0.26);
    this.randomConfigurator = new RandomConfigurator();
    this.gridConfigurator = new GridConfigurator(9);

  }

  /**
   * Method runs experiment.
   */
  public Double run() throws Exception {
    ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);

    // The taskQueue size must be limited since the results are stored in the task's reports
    // Queue -> Tasks -> Reports -> Solutions ==> OutOfMemoryError
    List<ExperimentTaskRequest> taskQueue = new ArrayList<>();

    // Prepare experiment task configs
    ProblemConfig config = defaultConfigurator.prepareConfigs(problemInfo,
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
        this.bestScore = taskScore;
      }
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }
}
