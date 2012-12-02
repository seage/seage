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
 */

package org.seage.experimenter;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IProblemProvider;
import org.seage.aal.algorithm.ProblemProvider;
import org.seage.aal.data.AlgorithmParams;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.seage.data.xml.XmlHelper;

/**
 * Runs experiment and provides following experiment log:
 * 
 * ExperimentTask 			# version 0.1 
 * |_ ... 
 * 
 * ExperimentReport 		# version 0.2
 *  |_ version (0.4)
 *  |_ experimentID
 *  |_ startTimeMS
 *  |_ timeoutS
 *  |_ durationS
 *  |_ machineName
 *  |_ nrOfCores
 *  |_ totalRAM
 *  |_ availRAM 
 *  |_ Config
 *  |	|_ configID
 *  |	|_ runID
 *  |	|_ Problem
 *  |	|	|_ problemID
 *  |	|	|_ Instance
 *  |	|		|_ name
 *  |	|_ Algorithm
 *  |		|_ algorithmID
 *  |		|_ Parameters
 *  |_ AlgorithmReport
 *  	|_ Parameters
 *  	|_ Statistics
 *  	|_ Minutes
 * 
 * @author rick
 */
class ExperimentTask implements Runnable{
	private static Logger _logger = Logger.getLogger(ExperimentTask.class.getName());
    private ProblemConfig _config;
    private long _experimentID;
    private String _reportName;
    private int _runID;
    private long _timeout = 9000;
    private ZipOutputStream _outputStream;
    
    private DataNode _experimentReport;
    
    //private static long _runOrder=100000;

    public ExperimentTask(long experimentID, int runID, String reportName, long timeoutS, ProblemConfig config, ZipOutputStream outputStream) throws Exception
    {
        _experimentID = experimentID;
    	_runID = runID;
        _reportName = reportName;
        _config = config;
        _timeout = timeoutS*1000;
        _outputStream = outputStream;

        _experimentReport = new DataNode("ExperimentTaskReport");
        _experimentReport.putValue("version", "0.4");
        _experimentReport.putValue("experimentID", experimentID);
        _experimentReport.putValue("timeoutS", timeoutS);
        try
		{
			_experimentReport.putValue("machineName", java.net.InetAddress.getLocalHost().getHostName());
		}
		catch (UnknownHostException e)
		{
			_logger.log(Level.WARNING, e.getMessage());
		}
        _experimentReport.putValue("nrOfCores", Runtime.getRuntime().availableProcessors());
        _experimentReport.putValue("totalRAM", Runtime.getRuntime().totalMemory());
        _experimentReport.putValue("availRAM", Runtime.getRuntime().maxMemory());
        
        DataNode configNode = new DataNode("Config");        
		configNode.putValue("configID", _config.getValueStr("configID"));		
        configNode.putValue("runID", _runID);
        
        DataNode problemNode = new DataNode("Problem");
        problemNode.putValue("problemID", _config.getDataNode("Problem").getValueStr("id"));
        
        DataNode instanceNode = new DataNode("Instance");
        instanceNode.putValue("name", _config.getDataNode("Problem").getDataNode("Instance").getValue("name"));
        
        DataNode algorithmNode = new DataNode("Algorithm");
        algorithmNode.putValue("algorithmID", _config.getDataNode("Algorithm").getValueStr("id"));
        algorithmNode.putDataNode(_config.getDataNode("Algorithm").getDataNode("Parameters"));

        problemNode.putDataNode(instanceNode);
        configNode.putDataNode(problemNode);
        configNode.putDataNode(algorithmNode);
        _experimentReport.putDataNode(configNode);
        
    }
    
    public void run() {
        String problemID = "";
        String algorithmID = "";
        try{
            problemID = _config.getDataNode("Problem").getValueStr("id");
            algorithmID = _config.getDataNode("Algorithm").getValueStr("id");

            // provider and factory
            IProblemProvider provider = ProblemProvider.getProblemProviders().get(problemID);
            IAlgorithmFactory factory = provider.getAlgorithmFactory(algorithmID);

            // problem instance
            ProblemInstanceInfo instance = provider.initProblemInstance(_config);
            instance.toString();
            // algorithm
            IAlgorithmAdapter algorithm = factory.createAlgorithm(instance, _config);

            AlgorithmParams algNode = _config.getAlgorithmParams();
            Object[][] solutions = provider.generateInitialSolutions(algNode.getDataNode("Parameters").getValueInt("numSolutions"), instance, _experimentID);

            long startTime = System.currentTimeMillis();
            algorithm.solutionsFromPhenotype(solutions);
            algorithm.setParameters(algNode);
            algorithm.startSearching(true);
            waitForTimeout(algorithm);
            algorithm.stopSearching();
            long endTime = System.currentTimeMillis();

            solutions = algorithm.solutionsToPhenotype();

            _experimentReport.putDataNode(algorithm.getReport());
            _experimentReport.putValue("durationS", (endTime - startTime)/1000);
            
            XmlHelper.writeXml(_experimentReport, _outputStream, new ZipEntry(_reportName));
            //serialize(expReport);

            //System.out.printf("%s %15s\t %20s\t %20s\n", algorithmID, instance.toString(), result[0], configID);
        }
        catch(Exception ex){
            //System.err.println("ERR: " + problemID +"/"+algorithmID+"/"+instanceName);
            //System.err.println(_config.toString());
            //ex.printStackTrace();
        	_logger.log(Level.SEVERE, ex.getMessage());
        }
    }
    
    private void waitForTimeout(IAlgorithmAdapter alg) throws Exception
    {
        long time = System.currentTimeMillis();
        while(alg.isRunning() && ((System.currentTimeMillis()-time)<_timeout))
            Thread.sleep(100);
    }

}
