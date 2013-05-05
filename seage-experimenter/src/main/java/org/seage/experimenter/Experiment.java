package org.seage.experimenter;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;


public abstract class Experiment implements Runnable
{
	protected static Logger _logger = Logger.getLogger(Experiment.class.getName());
	protected ProblemConfig _config;
	protected String _experimentType;
	protected long _experimentID;
	protected long _runID;
	protected long _timeoutS;
	
	protected String _reportName;
	protected DataNode _experimentReport;
	protected ZipOutputStream _reportOutputStream;
	
	
	public Experiment(String experimentType, long experimentID, long runID, long timeoutS, ProblemConfig config, String reportName, ZipOutputStream reportOutputStream) throws Exception
	{
		_experimentType = experimentType;
		_experimentID = experimentID;
		_runID = runID;
		_timeoutS = timeoutS;
		_config = config;
		_reportName = reportName;
        _reportOutputStream = reportOutputStream;    	
        
		_experimentReport = new DataNode("ExperimentTaskReport");
        _experimentReport.putValue("version", "0.5");
        _experimentReport.putValue("experimentType", experimentType);
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
	

}
