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
 * 	   Richard Malek
 * 	   - Interface definition
 */
package org.seage.experimenter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.InstanceInfo;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemProvider;
import org.seage.data.DataNode;

public abstract class Experimenter 
{
	protected static Logger _logger = Logger.getLogger(Experimenter.class.getName());
	protected String _experimentName;
	
	public Experimenter(String experimentName)
	{
		_experimentName = experimentName;
		
		new File("output/experiment-logs").mkdirs();
	}
	
    public void runFromConfigFile(String configPath) throws Exception
    {
    	throw new Exception("Not implemented");
    }

    /*public void runExperiment( int numOfConfigs, long timeoutS, String problemID) throws Exception
    {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();

        List<String> algIDs = new ArrayList<String>();
        for (DataNode alg : pi.getDataNode("Algorithms").getDataNodes("Algorithm"))
            algIDs.add(alg.getValueStr("id"));

        runExperiment(numOfConfigs, timeoutS, problemID, algIDs.toArray(new String[] {}));
    }

    public void runExperiment(int numOfConfigs, long timeoutS, String problemID, String[] algorithmIDs) throws Exception
    {
        ProblemInfo pi = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
        
        List<String> instanceIDs = new ArrayList<String>();
        for (DataNode ins : pi.getDataNode("Instances").getDataNodes("Instance"))
            instanceIDs.add(ins.getValueStr("id"));
        
        runExperiment(numOfConfigs, timeoutS, problemID, algorithmIDs, instanceIDs.toArray(new String[] {}));
        
    }*/
    
    public void runExperiment(String problemID, String[] instanceIDs, String[] algorithmIDs) throws Exception
    {
    	ProblemInfo problemInfo = ProblemProvider.getProblemProviders().get(problemID).getProblemInfo();
    	
    	// *** Check arguments ***
    	if(instanceIDs[0].equals("*"))
    	{
    		List<String> instIDs = new ArrayList<String>();
    		for (DataNode ins : problemInfo.getDataNode("Instances").getDataNodes("Instance"))    		
            	instIDs.add(ins.getValueStr("id"));
            instanceIDs = instIDs.toArray(new String[]{});
    	}
    	
    	if(algorithmIDs[0].equals("*"))
    	{
    		List<String> algIDs = new ArrayList<String>();
    		for (DataNode alg : problemInfo.getDataNode("Algorithms").getDataNodes("Algorithm"))
            	algIDs.add(alg.getValueStr("id"));
            algorithmIDs = algIDs.toArray(new String[]{});
    	}
    	// ***********************
    	
    	long experimentID = System.currentTimeMillis();
        _logger.info("Experiment "+experimentID+" started ...");
        _logger.info("-------------------------------------");
        //_logger.info("Mem: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
        
        long totalNumOfConfigs = 0;//numOfConfigs*algorithmIDs.length*instanceIDs.length;
        long totalNumberOfRuns = totalNumOfConfigs * 5; 
        long totalRunsPerCpu = totalNumberOfRuns / Runtime.getRuntime().availableProcessors();
        long totalEstimatedTime = 0;//totalRunsPerCpu * timeoutS;
        
        _logger.info("Total number of configs: " + totalNumOfConfigs);
        _logger.info("Total number of runs: " + totalNumberOfRuns);
        _logger.info("Total runs per cpu core: " + totalRunsPerCpu);
        //_logger.info("Total estimated time: " + estimatedTime + "s");
        _logger.info("Total estimated time: " + getDurationBreakdown(totalEstimatedTime * 1000) + " (DD:HH:mm:ss)");
        _logger.info("-------------------------------------");
        for(int i=0;i<instanceIDs.length;i++)
        {
        	try
        	{
	            InstanceInfo instanceInfo =  problemInfo.getInstanceInfo(instanceIDs[i]); 
	                          
	            _logger.info("-------------------------------------");
	            _logger.info(String.format("%-15s %s","Problem:", problemID));
	            _logger.info(String.format("%-15s %s    (%d/%d)", "Instance:", instanceIDs[i], i+1, instanceIDs.length));
	            //_logger.info(String.format("Algorithm: %s (%d/%d)", algorithmIDs[j], j, algorithmIDs.length));                
	            //_logger.info("Number of runs: " + numOfConfigs*5);
	            //_logger.info("Memory used for configs: " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
	
	            
	            String reportPath = String.format("output/experiment-logs/%s-%s-%s.zip", experimentID, problemID, instanceIDs[i]);
	
	            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath)));
	
	            performExperiment(experimentID, problemInfo, instanceInfo, algorithmIDs, zos);

	            zos.close();
        	}
        	catch(Exception ex)
        	{
        		_logger.log(Level.WARNING, ex.getMessage(), ex);
        	}
            
        }
        _logger.info("-------------------------------------");
        _logger.info("Experiment " + experimentID + " finished ...");
        _logger.info(String.format("Experiment duration: %s (DD:HH:mm:ss)", getDurationBreakdown(System.currentTimeMillis()-experimentID)));
    }
    
    protected abstract void performExperiment(long experimentID, ProblemInfo problemInfo, InstanceInfo instanceInfo, String[] algorithmIDs, ZipOutputStream zos) throws Exception;
	    
    
    protected static String getDurationBreakdown(long millis)
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
