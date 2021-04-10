package org.seage.hh.experimenter2;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.aal.problem.ProblemScoreCalculator;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTask;
import org.seage.hh.experimenter.ExperimentTaskRequest;
import org.seage.hh.experimenter.configurator.DefaultConfigurator;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MetaHeuristicExperimenter implements Experimenter {
  private static Logger logger =
      LoggerFactory.getLogger(MetaHeuristicExperimenter.class.getName());
  private DefaultConfigurator configurator;
  private ProblemInfo problemInfo;
  private IExperimentTasksRunner experimentTasksRunner;
  private ExperimentReporter experimentReporter;
  
  private UUID experimentID;
  private String problemID;
  private String instanceID;
  private String algorithmID;
  private int numRuns;
  private int timeoutS;
  private double initObjVal;
  private double bestObjVal;
  private UUID bestExperimentTaskID;


  /**
   * MetaHeuristicExperimenter constructor.
   */
  protected MetaHeuristicExperimenter(
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
    this.initObjVal = Double.MAX_VALUE;
    this.bestObjVal = Double.MAX_VALUE;

    // Initialize all
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    this.configurator = new DefaultConfigurator(0.15);
  }

  /**
   * Method runs experiment.
   */
  public Double runExperiment() throws Exception {
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
          algorithmID, config.getAlgorithmParams(), timeoutS));
    }

    // RUN EXPERIMENT TASKS
    experimentTasksRunner.performExperimentTasks(taskQueue, this::reportExperimentTask);
    
    
    ProblemScoreCalculator calculator = new ProblemScoreCalculator(problemInfo);
    // Calculate the score delta
    double initScore = calculator.calculateInstanceScore(instanceInfo.getInstanceID(), initObjVal);
    double bestScore = calculator.calculateInstanceScore(instanceInfo.getInstanceID(), bestObjVal);
    double scoreDelta = calculator.calculateScoreDelta(bestScore, initScore);

    
    // Report the experiment task's score
    experimentReporter.updateInstanceScore(bestExperimentTaskID, bestScore, scoreDelta);

    return bestObjVal;
  }
  
  private Void reportExperimentTask(ExperimentTask experimentTask) {
    try {
      experimentReporter.reportExperimentTask(experimentTask);
      setInitAndBestObjectiveValue(experimentTask.getExperimentTaskReport());
      
    } catch (Exception e) {
      logger.error(String.format("Failed to report the experiment task: %s", 
          experimentTask.getExperimentTaskID().toString()), e);
    }
    return null;
  }

  private void setInitAndBestObjectiveValue(DataNode experimentTaskReport) throws Exception {

    DataNode inputs = experimentTaskReport.getDataNode("Solutions").getDataNode("Input");
    for (DataNode s : inputs.getDataNodes("Solution")) {
      double objVal = s.getValueDouble("initObjVal");
      if (objVal < this.initObjVal) {
        this.initObjVal = objVal;
      }
    }

    DataNode outputs = experimentTaskReport.getDataNode("Solutions").getDataNode("Output");
    for (DataNode s : outputs.getDataNodes("Solution")) {
      double objVal = s.getValueDouble("bestObjVal");
      if (objVal < this.bestObjVal) {
        this.bestObjVal = objVal;
      }
    }
  }
}
