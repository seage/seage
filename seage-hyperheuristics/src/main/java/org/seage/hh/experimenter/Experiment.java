package org.seage.hh.experimenter;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.seage.hh.runner.LocalExperimentTasksRunner;
import org.seage.logging.TimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class for Experiment.
 */
public abstract class Experiment {
  protected static Logger logger = LoggerFactory.getLogger(Experiment.class.getName());

  public abstract void run() throws Exception;

  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected List<String> instanceIDs;
  protected String algorithmID;
  protected int timeoutS;
  // protected double experimentScore;
  protected String tag;
  private long startDate;
  private long endDate;

  /**
   * Experimenter constructor.
   *
   * @param experimentName Experiment Name.
   * @param algorithmID Algorithm ID.
   * @param problemID Problem ID
   * @param instanceIDs List of problem instances
   * @param timeoutS Timeout in seconds
   *
   * @throws Exception Exception
   */
  protected Experiment(String experimentName, String algorithmID, String problemID, List<String> instanceIDs, int timeoutS,
      String tag) throws Exception {
    this.experimentID = UUID.randomUUID();
    this.experimentName = experimentName;
    this.algorithmID = algorithmID;
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.timeoutS = timeoutS;
    this.tag = tag;
  }

  protected void logStart() {
    startDate = System.currentTimeMillis();
    logger.info("---------------------------------------------------");
    logger.info("AlgorithmID:   ### {} ###", algorithmID);
    logger.info("---------------------------------------------------");
    logger.info("ExperimentTag: {}", this.tag);
    logger.info("ExperimentID:  {}", experimentID);
    logger.info("---------------------------------------------------");
  }

  protected void logEnd(double experimentScore) {
    endDate = System.currentTimeMillis();
    long duration = endDate - startDate;
    String time = TimeFormat.getTimeDurationBreakdown(duration);
    logger.info("---------------------------------------------------");
    logger.info("Experiment done: {}", experimentID);
    logger.info("Experiment duration: {} (DD:HH:mm:ss)", time);
    logger.info("Experiment score: ### {} ###", experimentScore);
  }

  protected void createExperimentReport() throws Exception {
    ExperimentReporter.createExperimentReport(experimentID, experimentName,
        new String[] {problemID}, instanceIDs.toArray(new String[0]), new String[] {algorithmID},
        "TODO: Move to table 'runs'", Date.from(Instant.now()), tag);
  }
}
