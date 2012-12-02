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

package org.seage.experimenter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import org.seage.aal.data.ProblemConfig;
import org.seage.data.xml.XmlHelper;
import org.seage.thread.TaskRunner;

/**
 * 
 * @author Richard Malek
 */
public class ExperimentRunner
{

    @SuppressWarnings("unused")
	private static Logger _logger = Logger.getLogger(ExperimentRunner.class.getName());
    private int _numExperimentAttempts = 5;
	private int _processorCount;

	
    public ExperimentRunner()
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
        return run(new ProblemConfig[] { config }, System.currentTimeMillis(), problemID, algorithmID, instanceID, timeoutS);
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
    public long run(ProblemConfig[] configs, long experimentID, String problemID, String algorithmID, String instanceID, long timeoutS) throws Exception
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
                taskQueue.add(new ExperimentTask(experimentID, runID, reportName, timeoutS, config, zos));
            }
        }

        // Run threads for each processor
        new TaskRunner().runTasks(taskQueue, _processorCount);

        zos.close();
        fos.close();

        return experimentID;
    }

}
