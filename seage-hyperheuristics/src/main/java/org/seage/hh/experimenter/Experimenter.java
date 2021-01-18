/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Interface definition
 */

package org.seage.hh.experimenter;

import java.io.File;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.experimenter.runner.IExperimentTasksRunner;
import org.seage.hh.experimenter.runner.LocalExperimentTasksRunner;
// import org.seage.hh.experimenter.runner.SparkExperimentTasksRunner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Experimenter {
  protected static Logger logger = LoggerFactory.getLogger(Experimenter.class.getName());
  protected String experimentName;
  protected String experimentID;
  protected String problemID;
  protected String[] instanceIDs;
  protected String[] algorithmIDs;
  protected ProblemInfo problemInfo;
  protected IExperimentTasksRunner experimentTasksRunner;
  protected ExperimentReporter experimentReporter;

  /**
   * Experimenter performs experiment tasks.
   * @param experimentName Name of the experiment, e.g. SingleAlgoritmhRandom
   * @param problemID Problem identifier, e.g TSP, SAT or JSSP
   * @param instanceIDs Array of instance identifiers
   * @param algorithmIDs Array of algorithms involved in the experiment
   * @throws Exception Throws exception in case of a trouble.
   */
  public Experimenter(
      String experimentName, String problemID, String[] instanceIDs, String[] algorithmIDs)
      throws Exception {
    this.experimentName = experimentName;
    this.experimentID = String.valueOf(System.currentTimeMillis());
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.algorithmIDs = algorithmIDs;

    new File("output/experiment-logs").mkdirs();
    logger.info("Experimenter created, getting problem info");
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    //this.experimentTasksRunner = new SparkExperimentTasksRunner();

    this.experimentReporter = new ExperimentReporter();
  }

  public void runFromConfigFile(String configPath) throws Exception {
    throw new Exception("Not implemented");
  }

  /**
   * The entry point of the experiment.
   * @throws Exception Throws exception in case of a trouble.
   */
  public final void runExperiment() throws Exception {

    // *** Check arguments ***
    // Problem instances
    if (this.instanceIDs[0].equals("-")) {
      List<String> instIDs = new ArrayList<String>();
      for (DataNode ins : this.problemInfo.getDataNode("Instances").getDataNodes("Instance")) {
        instIDs.add(ins.getValueStr("id"));
      }
      this.instanceIDs = instIDs.toArray(new String[] {});
    }
    // Algorithms
    if (this.algorithmIDs[0].equals("-")) {
      List<String> algIDs = new ArrayList<String>();
      for (DataNode alg : this.problemInfo.getDataNode("Algorithms").getDataNodes("Algorithm")) {
        algIDs.add(alg.getValueStr("id"));
      }
      this.algorithmIDs = algIDs.toArray(new String[] {});
    }
    // ***********************

    long experimentID = System.currentTimeMillis();
    logger.info("-------------------------------------");
    logger.info("Experimenter: " + this.experimentName);
    logger.info("ExperimentID: " + experimentID);
    logger.info("-------------------------------------");

    long totalNumOfConfigs = getNumberOfConfigs(this.instanceIDs.length, this.algorithmIDs.length);
    long totalRunsPerCpu = totalNumOfConfigs / Runtime.getRuntime().availableProcessors();
    long totalEstimatedTime = getEstimatedTime(this.instanceIDs.length, this.algorithmIDs.length)
        / Runtime.getRuntime().availableProcessors();

    logger.info(String.format("%-25s: %s", "Total number of configs", totalNumOfConfigs));
    logger.info("Total number of configs per cpu core: " + totalRunsPerCpu);
    logger.info(String.format("Total estimated time: %s (DD:HH:mm:ss)", 
        getDurationBreakdown(totalEstimatedTime)));
    logger.info("-------------------------------------");

    this.experimentReporter.createExperimentReport(
        this.experimentID,
        this.experimentName,
        this.problemID,
        this.instanceIDs,
        this.algorithmIDs,
        Date.from(Instant.now()),
        Date.from(Instant.now())
    );

    // Run experiment tasks for each instance
    for (int i = 0; i < this.instanceIDs.length; i++) {
      try {
        logger.info("-------------------------------------");
        logger.info(String.format("%-15s %s", "Problem:", this.problemID));
        logger.info(String.format("%-15s %-16s    (%d/%d)", "Instance:", 
            this.instanceIDs[i], i + 1, this.instanceIDs.length));
        ProblemInstanceInfo instanceInfo = this.problemInfo.getProblemInstanceInfo(
            this.instanceIDs[i]);
        runExperiment(instanceInfo);
      } catch (Exception ex) {
        logger.warn(ex.getMessage(), ex);
      }
    }
    logger.info("-------------------------------------");
    logger.info("Experiment " + experimentID + " finished ...");
    logger.info(String.format("Experiment duration: %s (DD:HH:mm:ss)",
        getDurationBreakdown(System.currentTimeMillis() - experimentID)));
  }

  protected abstract void runExperiment(ProblemInstanceInfo instanceInfo) throws Exception;

  protected abstract long getEstimatedTime(int instancesCount, int algorithmsCount);

  protected abstract long getNumberOfConfigs(int instancesCount, int algorithmsCount);

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
}
