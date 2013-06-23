package org.seage.experimenter;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;


public abstract class ExperimentTask implements Runnable
{
	protected static Logger _logger = Logger.getLogger(ExperimentTask.class.getName());
	protected ProblemConfig _config;
	protected String _experimentType;
	protected long _experimentID;
	protected long _runID;
	protected long _timeoutS;
	
	protected String _reportName;
	protected DataNode _experimentTaskReport;
	protected ZipOutputStream _reportOutputStream;
	
	
	public ExperimentTask(String experimentType, long experimentID, long runID, long timeoutS, ProblemConfig config, String reportName, ZipOutputStream reportOutputStream) throws Exception
	{
		_experimentType = experimentType;
		_experimentID = experimentID;
		_runID = runID;
		_timeoutS = timeoutS;
		_config = config;
		_reportName = reportName;
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
        
        _experimentTaskReport.putDataNode(configNode);
		
	}
	

}
