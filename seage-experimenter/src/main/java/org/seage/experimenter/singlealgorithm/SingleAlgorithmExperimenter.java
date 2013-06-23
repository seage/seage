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
package org.seage.experimenter.singlealgorithm;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInfo;
import org.seage.data.DataNode;
import org.seage.experimenter.Experimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.thread.TaskRunnerEx;

/**
 * 
 * @author rick
 */
public abstract class SingleAlgorithmExperimenter extends Experimenter
{
	Configurator _configurator;    

    public SingleAlgorithmExperimenter(String experimentName, Configurator configurator)
    {
    	super(experimentName);
        _configurator = configurator;
    }

 
    public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs, String[] instanceIDs) throws Exception
    {
        long experimentID = System.currentTimeMillis();
        _logger.info("Experiment "+experimentID+" started ...");
        _logger.info("-------------------------------------");
        //_logger.info("Mem: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));

        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        long totalNumOfConfigs = numOfConfigs*algorithmIDs.length*instanceIDs.length;
        long totalNumberOfRuns = totalNumOfConfigs * 5; 
        long totalRunsPerCpu = totalNumberOfRuns / Runtime.getRuntime().availableProcessors();
        long totalEstimatedTime = totalRunsPerCpu * timeoutS;
        
        _logger.info("Total number of configs: " + totalNumOfConfigs);
        _logger.info("Total number of runs: " + totalNumberOfRuns);
        _logger.info("Total runs per cpu core: " + totalRunsPerCpu);
        //_logger.info("Total estimated time: " + estimatedTime + "s");
        _logger.info("Total estimated time: " + getDurationBreakdown(totalEstimatedTime * 1000) + " (DD:HH:mm:ss)");
        _logger.info("-------------------------------------");
        int i=0, j=0;
        for (String algorithmID : algorithmIDs)
        {
            i++;j=0;
            for (String instanceID : instanceIDs)
            {
                j++;
                DataNode instanceInfo =  pi.getDataNode("Instances").getDataNodeById(instanceID);
                //List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
                //configs.addAll(Arrays.asList());                
                _logger.info("-------------------------------------");
                _logger.info(String.format("Instance: %s (%d/%d)", instanceID, j, instanceIDs.length));
                _logger.info(String.format("Algorithm: %s (%d/%d)", algorithmID, i, algorithmIDs.length));                
                _logger.info("Number of runs: " + numOfConfigs*5);
                //_logger.info("Memory used for configs: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));

                
                String reportPath = String.format("output/experiment-logs/%s-%s-%s-%s.zip", experimentID, problemID, algorithmID, instanceID);

                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath)));

                // Create a task queue
                List<Runnable> taskQueue = new ArrayList<Runnable>();
                for(ProblemConfig config : _configurator.prepareConfigs(pi, algorithmID, instanceInfo, numOfConfigs))
                {
                	String configID = config.getConfigID();

                    for (int runID = 1; runID <= 5; runID++)
                    {
                        String reportName = problemID + "-" + algorithmID + "-" + instanceID + "-" + configID + "-" + runID + ".xml";
                        taskQueue.add(new SingleAlgorithmExperimentTask(_experimentName, experimentID, timeoutS, config, reportName, zos, runID));
                    }
                }
                new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(taskQueue.toArray(new Runnable[]{}));

                zos.close();            
            }
        }
        _logger.info("-------------------------------------");
        _logger.log(Level.INFO, "Experiment " + experimentID + " finished ...");
    }
}
