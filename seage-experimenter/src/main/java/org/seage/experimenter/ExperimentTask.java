package org.seage.experimenter;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.data.DataNode;


public abstract class ExperimentTask implements Runnable
{
	protected static Logger _logger = Logger.getLogger(ExperimentTask.class.getName());
	//protected ProblemConfig _config;
	protected String _experimentType;
	protected long _experimentID;
	protected String _problemID;
	protected String _instanceID;
	protected String _algorithmID;
	protected AlgorithmParams _algorithmParams;
	protected long _runID;
	protected long _timeoutS;
	
	//protected String _reportName;
	protected DataNode _experimentTaskReport;
	protected ZipOutputStream _reportOutputStream;
	
	public ExperimentTask(String experimentType, long experimentID, String problemID, String instanceID, String algorithmID, AlgorithmParams algorithmParams, int runID, long timeoutS, ZipOutputStream reportOutputStream) throws Exception
	{
		_experimentType = experimentType;
		_experimentID = experimentID;
		_problemID = problemID;
		_instanceID = instanceID;
		_algorithmID = algorithmID;
		_algorithmParams = algorithmParams;
		_runID = runID;
		_timeoutS = timeoutS;
		//_config = config;
		//_reportName = reportName;
        _reportOutputStream = reportOutputStream;    	
        
		_experimentTaskReport = new DataNode("ExperimentTaskReport");
        _experimentTaskReport.putValue("version", "0.5");
        _experimentTaskReport.putValue("experimentType", experimentType);
        _experimentTaskReport.putValue("experimentID", experimentID);
        _experimentTaskReport.putValue("timeoutS", timeoutS);
        
        try
		{
			_experimentTaskReport.putValue("machineName", java.net.InetAddress.getLocalHost().getHostName());
		}
		catch (UnknownHostException e)
		{
			_logger.log(Level.WARNING, e.getMessage());
		}
        _experimentTaskReport.putValue("nrOfCores", Runtime.getRuntime().availableProcessors());
        _experimentTaskReport.putValue("totalRAM", Runtime.getRuntime().totalMemory());
        _experimentTaskReport.putValue("availRAM", Runtime.getRuntime().maxMemory());
        
        
        DataNode configNode = new DataNode("Config");        
		//configNode.putValue("configID", _config.getValueStr("configID"));
		configNode.putValue("runID", _runID);
        
        DataNode problemNode = new DataNode("Problem");
        problemNode.putValue("problemID", _problemID);
        
        DataNode instanceNode = new DataNode("Instance");
        instanceNode.putValue("name", _instanceID);
        
        DataNode algorithmNode = new DataNode("Algorithm");
        algorithmNode.putValue("algorithmID", _algorithmID);
        algorithmNode.putDataNode(_algorithmParams);

        problemNode.putDataNode(instanceNode);
        configNode.putDataNode(problemNode);
        configNode.putDataNode(algorithmNode);
        
        _experimentTaskReport.putDataNode(configNode);
		
	}	
	
	protected String getReportName() throws Exception
	{
		return _problemID + "-" + _instanceID + "-" + _algorithmID + "-" + _algorithmParams.hash() + "-" + _runID;
	}
	
	public DataNode getExperimentTaskReport()
	{
		return _experimentTaskReport;
	}

}
