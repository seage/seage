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
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.seage.aal.data.ProblemConfig;
import org.seage.data.xml.XmlHelper;
import org.seage.thread.TaskRunner;

/**
 * 
 * @author Richard Malek
 */
class ExperimentRunner
{

	private static Logger _logger = Logger.getLogger(ExperimentRunner.class.getName());

	private int _numExperimentAttempts = 5;

	/**
	 * 
	 * @param config
	 * @param timeoutS
	 * @return
	 * @throws Exception
	 */
	public long run(ProblemConfig config, long timeoutS) throws Exception
	{
		return run(new ProblemConfig[] { config }, timeoutS);
	}

	/**
	 * 
	 * @param configPath
	 * @param timeoutS
	 * @return
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
	 * @param timeoutS
	 * @return
	 * @throws Exception
	 */
	public long run(ProblemConfig[] configs, long timeoutS) throws Exception
	{
		long experimentID = System.currentTimeMillis();
		
		FileOutputStream fos = new FileOutputStream(new File("output/experiment-logs/"+experimentID+".zip"));
		ZipOutputStream zos = new ZipOutputStream(fos);

		// Create a task queue
		List<Runnable> taskQueue = new ArrayList<Runnable>();
		for (ProblemConfig config : configs)
		{
			String problemID = config.getProblemID();
			String instanceName = config.getInstanceName().split("\\.")[0];
			String algorithmID = config.getAlgorithmID();
			
			for (int runID = 1; runID <= _numExperimentAttempts; runID++)
			{				
				String entryName = experimentID+"-"+problemID +"-"+algorithmID+"-"+instanceName +"-"+runID+".xml";
				taskQueue.add(new ExperimentTask(experimentID, entryName, timeoutS, config, zos));
			}
		}

		// Run threads for each processor	
		new TaskRunner().runTasks(taskQueue, Runtime.getRuntime().availableProcessors());
		
		zos.close();
		fos.close();
		
		return experimentID;
	}

	
}
