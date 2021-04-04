package org.seage.hh.experimenter2;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.ExperimentReporter;
import org.seage.hh.experimenter.ExperimentTask;
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
  protected ExperimentReporter experimentReporter;
  protected IExperimentTasksRunner experimentTasksRunner;

  private static final int NUM_RUNS = 3;
  
  protected UUID experimentID;
  protected String experimentName;
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
      String algorithmID, int numConfigs, int timeoutS) 
      throws Exception {
    this.experimentName = "MetaHeruristicApproach";
    this.experimentID = experimentID;
    this.problemID = problemID;
    this.instanceID = instanceID;
    this.algorithmID = algorithmID;
    this.numConfigs = numConfigs;
    this.timeoutS = timeoutS;

    logger.info("Experimenter {} created, getting problem info", experimentID);
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    this.experimentReporter = new ExperimentReporter();
    this.configurator = new DefaultConfigurator(0.15);
  }


  /**
   * Method runs experiment.
   */
  public void runExperiment() {
    logger.info("Running MetaheuristicExperimenter");
    try {
      this.experimentID = UUID.randomUUID();
      logger.info("-------------------------------------");
      logger.info("Experimenter: {}", this.experimentName);
      logger.info("ExperimentID: {}", experimentID);
      logger.info("-------------------------------------");

      long totalNumOfConfigs = getNumberOfConfigs(1, 1);
      long totalRunsPerCpu = (long)Math.ceil(
          (double)totalNumOfConfigs / Runtime.getRuntime().availableProcessors());
      long totalEstimatedTime = getEstimatedTime(1, 1);

      logger.info(String.format("%-25s: %s", "Total number of configs", totalNumOfConfigs));
      logger.info("Total number of configs per cpu core: " + totalRunsPerCpu);
      logger.info(String.format("Total estimated time: %s (DD:HH:mm:ss)", 
          getDurationBreakdown(totalEstimatedTime)));
      logger.info("-------------------------------------");

      // Create experiment reporter
      this.experimentReporter.createExperimentReport(
          this.experimentID,
          this.experimentName,
          this.problemID,
          new String[] {this.instanceID},
          new String[] {this.algorithmID},
          getExperimentConfig(),
          Date.from(Instant.now())
      );

      // Run experiment
      logger.info("-------------------------------------");
      logger.info(String.format("%-15s %s", "Problem:", problemID));
      logger.info(String.format("Instance: " + instanceID));

      ProblemInstanceInfo instanceInfo = problemInfo.getProblemInstanceInfo(instanceID);
      runExperimentTasksForProblemInstance(instanceInfo);
      // end experiment

      // Inform about the duration
      long startDate = System.currentTimeMillis();
      long endDate = startDate;
      endDate = System.currentTimeMillis();
      logger.info("-------------------------------------");
      logger.info("Experiment {} finished ...", experimentID);
      logger.info("Experiment duration: {} (DD:HH:mm:ss)", 
          getDurationBreakdown(endDate - startDate));
      
      this.experimentReporter.updateEndDate(this.experimentID, new Date(endDate));
    } catch (Exception ex) {
      logger.warn(ex.getMessage(), ex);
    }
  }


  protected void runExperimentTasksForProblemInstance(
      ProblemInstanceInfo instanceInfo) throws Exception {
    
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



  protected String getExperimentConfig() {
    DataNode config = new DataNode("Config");
    config.putValue("timeoutS", this.timeoutS);
    config.putValue("numConfigs", this.numConfigs);
    
    return config.toString();
  }


  protected static String getDurationBreakdown(long millis) {
    if (millis < 0) {
      throw new IllegalArgumentException("Duration must be greater than zero!");
    }

    long days = TimeUnit.MILLISECONDS.toDays(millis);
    millis -= TimeUnit.DAYS.toMillis(days);
    long hours = TimeUnit.MILLISECONDS.toHours(millis);
    millis -= TimeUnit.HOURS.toMillis(hours);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
    millis -= TimeUnit.MINUTES.toMillis(minutes);
    long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

    return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds); // (sb.toString());
  }
  

  protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) {
    return (long)this.numConfigs * NUM_RUNS * instancesCount * algorithmsCount;
  }


  protected long getEstimatedTime(int instancesCount, int algorithmsCount) {
    return (long)Math.ceil((double)getNumberOfConfigs(instancesCount, algorithmsCount) 
        / Runtime.getRuntime().availableProcessors())
        * this.timeoutS * 1000;
  }
}
