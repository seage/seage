package org.seage.hh.experimenter;

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

  public abstract ExperimentScoreCard run() throws Exception;

  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected List<String> instanceIDs;
  protected String algorithmID;
  protected int timeoutS;
  //protected double experimentScore;
  protected String tag;

  /**
   * Experimenter constructor.
   *
   * @param algorithmID Algorithm ID.
   * @param problemID Problem ID
   * @param instanceIDs List of problem instances
   * @param timeoutS Timeout in seconds
   *
   * @throws Exception Exception
   */
  protected Experiment(
      String algorithmID, String problemID, List<String> instanceIDs, int timeoutS, String tag) 
      throws Exception {
    this.experimentID = UUID.randomUUID();
    this.algorithmID = algorithmID;
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.timeoutS = timeoutS;
    this.tag = tag;
  }

  protected void logStart() {
    logger.info("---------------------------------------------------");
    logger.info("AlgorithmID:   ### {} ###", algorithmID);
    logger.info("---------------------------------------------------");
    logger.info("ExperimentTag: {}", this.tag);
    logger.info("ExperimentID:  {}", experimentID);
    logger.info("---------------------------------------------------");
  }

  protected void logEnd(long duration, double experimentScore) {
    String time = TimeFormat.getTimeDurationBreakdown(duration);
    logger.info("---------------------------------------------------");
    logger.info("Experiment {} done", experimentID);
    logger.info("Experiment duration: {} (DD:HH:mm:ss)", time);
    logger.info("Experiment score: ### {} ###", experimentScore);
  }
}
