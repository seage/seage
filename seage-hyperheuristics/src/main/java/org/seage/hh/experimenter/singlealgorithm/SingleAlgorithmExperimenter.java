/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     Jan Zmatlik
 *     - Modified
 */
package org.seage.hh.experimenter.singlealgorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipOutputStream;

import org.seage.hh.experimenter.Experimenter;

/**
 * 
 * @author Richard Malek
 */
public abstract class SingleAlgorithmExperimenter extends Experimenter {

//    public SingleAlgorithmExperimenter(String experimentName, String problemID, String[] instanceIDs, String[] algorithmIDs, int numConfigs, int timeoutS)
//    {
//        super(experimentName);
//    }

  public SingleAlgorithmExperimenter(String experimentName, String problemID, String[] instanceIDs,
      String[] algorithmIDs) throws Exception {
    super(experimentName, problemID, instanceIDs, algorithmIDs);
    // TODO Auto-generated constructor stub
  }

  public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs,
      String[] instanceIDs) throws Exception {
    long experimentID = System.currentTimeMillis();
    _logger.info("Experiment " + experimentID + " started ...");
    _logger.info("-------------------------------------");
    // _logger.info("Mem: " + (Runtime.getRuntime().totalMemory() -
    // Runtime.getRuntime().freeMemory()) / (1024 * 1024));

    // ProblemInfo pi =
    // ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

    long totalNumOfConfigs = numOfConfigs * algorithmIDs.length * instanceIDs.length;
    long totalNumberOfRuns = totalNumOfConfigs * 5;
    long totalRunsPerCpu = totalNumberOfRuns / Runtime.getRuntime().availableProcessors();
    long totalEstimatedTime = totalRunsPerCpu * timeoutS;

    _logger.info("Total number of configs: " + totalNumOfConfigs);
    _logger.info("Total number of runs: " + totalNumberOfRuns);
    _logger.info("Total runs per cpu core: " + totalRunsPerCpu);
    // _logger.info("Total estimated time: " + estimatedTime + "s");
    _logger.info("Total estimated time: " + getDurationBreakdown(totalEstimatedTime * 1000) + " (DD:HH:mm:ss)");
    _logger.info("-------------------------------------");
    int i = 0, j = 0;
    for (String algorithmID : algorithmIDs) {
      i++;
      j = 0;
      for (String instanceID : instanceIDs) {
        j++;
        // DataNode instanceInfo =
        // pi.getDataNode("Instances").getDataNodeById(instanceID);
        // List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
        // configs.addAll(Arrays.asList());
        _logger.info("-------------------------------------");
        _logger.info(String.format("Instance: %s (%d/%d)", instanceID, j, instanceIDs.length));
        _logger.info(String.format("Algorithm: %s (%d/%d)", algorithmID, i, algorithmIDs.length));
        _logger.info("Number of runs: " + numOfConfigs * 5);
        // _logger.info("Memory used for configs: " +
        // (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) /
        // (1024 * 1024));

        String reportPath = String.format("output/experiment-logs/%s-%s-%s-%s.zip", experimentID, problemID,
            algorithmID, instanceID);

        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath)));

        // Create a task queue

        zos.close();
      }
    }
    _logger.info("-------------------------------------");
    _logger.info("Experiment " + experimentID + " finished ...");
  }
}
