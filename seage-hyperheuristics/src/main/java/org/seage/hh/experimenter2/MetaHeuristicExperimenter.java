package org.seage.hh.experimenter2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaHeuristicExperimenter implements AlgorithmExperimenter {
  protected static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());
  protected DefaultConfigurator configurator;
  protected ProblemInfo problemInfo;
  protected IExperimentTasksRunner experimentTasksRunner;
  protected ExperimentReporter experimentReporter;
  
  protected UUID experimentID;
  protected String experimentName;
  protected String problemID;
  protected String instanceID;
  protected String algorithmID;
  protected int numRuns;
  protected int timeoutS;


  /**
   * MetaHeuristicExperimenter constructor.
   */
  protected MetaHeuristicExperimenter(
      UUID experimentID, String problemID, String instanceID, 
      String algorithmID, int numRuns, int timeoutS,
      ExperimentReporter experimentReporter) 
      throws Exception {
    this.experimentName = "MetaHeruristicApproach";
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.numRuns = numRuns;
    this.timeoutS = timeoutS;
    this.experimentReporter = experimentReporter;

    // Initialize all
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    this.configurator = new DefaultConfigurator(0.15);
  }

  /**
   * Method runs experiment.
   */
  public void runExperiment() {
    try {
      ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
      runExperimentTasksForProblemInstance(instanceInfo);

    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
  }


  protected void runExperimentTasksForProblemInstance(
      ProblemInstanceInfo instanceInfo) throws Exception {

    // Initialize the best value 
    double bestObjVal = Double.MAX_VALUE;

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
          algorithmID, config.getAlgorithmParams(), timeoutS));
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
