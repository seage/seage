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

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.seage.aal.problem.ProblemInfo;
//import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;
import org.seage.hh.runner.IExperimentTasksRunner;
import org.seage.hh.runner.LocalExperimentTasksRunner;
import org.seage.logging.TimeFormat;
// import org.seage.hh.runner.SparkExperimentTasksRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class OldExperimenter {
  protected static Logger logger = LoggerFactory.getLogger(OldExperimenter.class.getName());
  protected String experimentName;
  protected UUID experimentID;
  protected String problemID;
  protected String[] instanceIDs;
  protected String[] algorithmIDs;
  protected ProblemInfo problemInfo;
  protected IExperimentTasksRunner experimentTasksRunner;

  /**
   * Experimenter performs experiment tasks.
   * @param experimentName Name of the experiment, e.g. SingleAlgoritmhRandom
   * @param problemID Problem identifier, e.g TSP, SAT or JSP
   * @param instanceIDs Array of instance identifiers
   * @param algorithmIDs Array of algorithms involved in the experiment
   * @throws Exception Throws exception in case of a trouble.
   */
  protected OldExperimenter(
      String experimentName, String problemID, String[] instanceIDs, String[] algorithmIDs)
      throws Exception {
    this.experimentName = experimentName;
    this.experimentID = null;
    this.problemID = problemID;
    this.instanceIDs = instanceIDs;
    this.algorithmIDs = algorithmIDs;

    logger.info("Experimenter {} created, getting problem info", experimentName);
    this.problemInfo = ProblemProvider.getProblemProviders().get(this.problemID).getProblemInfo();
    this.experimentTasksRunner = new LocalExperimentTasksRunner();
    // this.experimentTasksRunner = new SparkExperimentTasksRunner();

  }

  public void runFromConfigFile(String configPath) throws Exception {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * The entry point of the experiment.
   * @throws Exception Throws exception in case of a trouble.
   */
  public void runExperiment() throws Exception {

    // *** Check arguments ***
    // Problem instances
    if (this.instanceIDs[0].equals("-")) {
      List<String> instIDs = new ArrayList<>();
      for (DataNode ins : this.problemInfo.getDataNode("Instances").getDataNodes("Instance")) {
        instIDs.add(ins.getValueStr("id"));
      }
      this.instanceIDs = instIDs.toArray(new String[] {});
    }
    // Algorithms
    if (this.algorithmIDs[0].equals("-")) {
      List<String> algIDs = new ArrayList<>();
      for (DataNode alg : this.problemInfo.getDataNode("Algorithms").getDataNodes("Algorithm")) {
        algIDs.add(alg.getValueStr("id"));
      }
      this.algorithmIDs = algIDs.toArray(new String[] {});
    }
    // ***********************

    long startDate = System.currentTimeMillis();
    long endDate;
    endDate = startDate;
    this.experimentID = UUID.randomUUID();
    logger.info("-------------------------------------");
    logger.info("Experimenter: {}", this.experimentName);
    logger.info("ExperimentID: {}", experimentID);
    logger.info("-------------------------------------");

    long totalNumOfConfigs = getNumberOfConfigs(this.instanceIDs.length, this.algorithmIDs.length);
    long totalRunsPerCpu = (long)Math.ceil(
        (double)totalNumOfConfigs / Runtime.getRuntime().availableProcessors());
    long totalEstimatedTime = getEstimatedTime(this.instanceIDs.length, this.algorithmIDs.length);

    logger.info(String.format("%-25s: %s", "Total number of configs", totalNumOfConfigs));
    logger.info("Total number of configs per cpu core: " + totalRunsPerCpu);
    logger.info(String.format("Total estimated time: %s (DD:HH:mm:ss)", 
        TimeFormat.getTimeDurationBreakdown(totalEstimatedTime)));
    logger.info("-------------------------------------");

    ExperimentReporter.createExperimentReport(
        this.experimentID,
        this.experimentName,
        new String[] {this.problemID},
        this.instanceIDs,
        this.algorithmIDs,
        getExperimentConfig(),
        Date.from(Instant.now()), "old-experimenter"
    );

    experimentMain();

    endDate = System.currentTimeMillis();
    logger.info("-------------------------------------");
    logger.info("Experiment {} finished ...", experimentID);
    logger.info("Experiment duration: {} (DD:HH:mm:ss)", 
        TimeFormat.getTimeDurationBreakdown(endDate - startDate));
    
    ExperimentReporter.updateEndDate(this.experimentID, new Date(endDate));
  }

  protected abstract void experimentMain() throws Exception;

  protected abstract String getExperimentConfig();

  protected abstract long getEstimatedTime(int instancesCount, int algorithmsCount);

  protected abstract long getNumberOfConfigs(int instancesCount, int algorithmsCount);

}
