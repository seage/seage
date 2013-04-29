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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.ProblemProvider;
import org.seage.experimenter.IExperimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.aal.data.ProblemInfo;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;
import org.seage.experimenter.singlealgorithm.random.RandomConfigurator;
import org.seage.thread.TaskRunner;

/**
 * 
 * @author rick
 */
public class SingleAlgorithmExperimenter implements IExperimenter
{

    protected static Logger _logger = Logger.getLogger(SingleAlgorithmExperimenter.class.getName());

    Configurator _configurator;
    SingleAlgorithmExperimentRunner _experimentRunner;
    String _experimentName;

    public SingleAlgorithmExperimenter(String experimentName, Configurator configurator)
    {
    	_experimentName= experimentName;
        _configurator = configurator;

        _experimentRunner = new SingleAlgorithmExperimentRunner();
    }

    public void runFromConfigFile(String configPath) throws Exception
    {
        _experimentRunner.run(configPath, Long.MAX_VALUE);
    }

    @Override
    public void runExperiment( int numOfConfigs, long timeoutS, String problemID) throws Exception
    {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        List<String> algIDs = new ArrayList<String>();
        for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes("Algorithm"))
            algIDs.add(alg.getValueStr("id"));

        runExperiment(numOfConfigs, timeoutS, problemID, algIDs.toArray(new String[] {}));
    }
    
    @Override
    public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs) throws Exception
    {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        List<String> instanceNames = new ArrayList<String>();
        for (DataNode ins : pi.getDataNode("Instances").getDataNodes("Instance"))
            instanceNames.add(ins.getValueStr("id"));
        
        runExperiment(numOfConfigs, timeoutS, problemID, algorithmIDs, instanceNames.toArray(new String[] {}));
        
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
        for (String algID : algorithmIDs)
        {
            i++;j=0;
            for (String instanceID : instanceIDs)
            {
                j++;
                DataNode instanceInfo =  pi.getDataNode("Instances").getDataNodeById(instanceID);
                List<ProblemConfig> configs = new ArrayList<ProblemConfig>();
                configs.addAll(Arrays.asList(_configurator.prepareConfigs(pi, algID, instanceInfo, numOfConfigs)));                
                _logger.info("-------------------------------------");
                _logger.info(String.format("Algorithm: %s (%d/%d)", algID, i, algorithmIDs.length));
                _logger.info(String.format("Instance: %s (%d/%d)", instanceID, j, instanceIDs.length));
                _logger.info("Number of runs: " + numOfConfigs*5);
                //_logger.info("Memory used for configs: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));

                _experimentRunner.run(configs.toArray(new ProblemConfig[] {}), _experimentName, experimentID, problemID, algID, instanceID, timeoutS);
            }
        }
        _logger.info("-------------------------------------");
        _logger.log(Level.INFO, "Experiment " + experimentID + " finished ...");
    }

    private static String getDurationBreakdown(long millis)
    {
        if (millis < 0) { throw new IllegalArgumentException("Duration must be greater than zero!"); }

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

class SingleAlgorithmExperimentRunner
{

    @SuppressWarnings("unused")
	private static Logger _logger = Logger.getLogger(SingleAlgorithmExperimentRunner.class.getName());
    private int _numExperimentAttempts = 5;
	private int _processorCount;

	
    public SingleAlgorithmExperimentRunner()
	{
		_processorCount = Runtime.getRuntime().availableProcessors();
    	//_processorCount = 1;
	}

	/**
     * 
     * @param config
     * @param timeoutS
     * @return experimentID
     * @throws Exception
     */
    public long run(ProblemConfig config, long timeoutS) throws Exception
    {
        String problemID = config.getProblemID();
        String algorithmID = config.getAlgorithmID();
        String instanceID = config.getInstanceName().split("\\.")[0];
        return run(new ProblemConfig[] { config }, "Config", System.currentTimeMillis(), problemID, algorithmID, instanceID, timeoutS);
    }

    /**
     * 
     * @param configPath
     * @param timeoutS
     * @return experimentID
     * @throws Exception
     */
    public long run(String configPath, long timeoutS) throws Exception
    {
        ProblemConfig config = new ProblemConfig(XmlHelper.readXml(new File(configPath)));
        return run(config, timeoutS);
    }

    /**
     * 
     * @param configs
     * @param instanceID
     * @param algorithmID
     * @param timeoutS
     * @return experimentID
     * @throws Exception
     */
    public long run(ProblemConfig[] configs, String experimentType,  long experimentID, String problemID, String algorithmID, String instanceID, long timeoutS) throws Exception
    {
        // long experimentID = System.currentTimeMillis();

        new File("output/experiment-logs").mkdirs();
        String reportPath = String.format("output/experiment-logs/%s-%s-%s-%s.zip", experimentID, problemID, algorithmID, instanceID);

        FileOutputStream fos = new FileOutputStream(new File(reportPath));
        ZipOutputStream zos = new ZipOutputStream(fos);

        // Create a task queue
        List<Runnable> taskQueue = new ArrayList<Runnable>();
        for (ProblemConfig config : configs)
        {
            String configID = config.getConfigID();
            // String problemID2 = config.getProblemID();
            // String instanceName2 = config.getInstanceName().split("\\.")[0];
            // String algorithmID2 = config.getAlgorithmID();

            for (int runID = 1; runID <= _numExperimentAttempts; runID++)
            {
                String reportName = problemID + "-" + algorithmID + "-" + instanceID + "-" + configID + "-" + runID + ".xml";
                taskQueue.add(new SingleAlgorithmExperiment(experimentType, experimentID, runID, reportName, timeoutS, config, zos));
            }
        }

        // Run threads for each processor
        new TaskRunner().runTasks(taskQueue, _processorCount);

        zos.close();
        fos.close();

        return experimentID;
    }

}
