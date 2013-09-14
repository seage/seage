package org.seage.experimenter.singlealgorithm.random;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.InstanceInfo;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.experimenter.Experimenter;
import org.seage.experimenter.config.Configurator;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimentTask;
import org.seage.thread.TaskRunnerEx;

public class SingleAlgorithmRandomExperimenter extends Experimenter
{
	protected Configurator _configurator;
	private int _numConfigs;
	private int _timeoutS;
	
	public SingleAlgorithmRandomExperimenter(int numConfigs, int timeoutS)
	{
		super("SingleAlgorithmRandom");
		
		_numConfigs = numConfigs;
		_timeoutS = timeoutS;
		
		_configurator = new RandomConfigurator();
	}
	
	protected SingleAlgorithmRandomExperimenter(String experimenterName, int numConfigs, int timeoutS)
	{
		this(numConfigs, timeoutS);
	}
	
	@Override
	protected void performExperiment(long experimentID, ProblemInfo problemInfo, InstanceInfo instanceInfo, String[] algorithmIDs, ZipOutputStream zos) throws Exception
	{
		for(String algorithmID : algorithmIDs)
		{
			String problemID = problemInfo.getProblemID();
        	String instanceID = instanceInfo.getInstanceID();
        	
			List<Runnable> taskQueue = new ArrayList<Runnable>();
	        for(ProblemConfig config : _configurator.prepareConfigs(problemInfo, instanceInfo.getInstanceID(), algorithmID, _numConfigs))
	        {
	            for (int runID = 1; runID <= 5; runID++)
	            {
	                //String reportName = problemInfo.getProblemID() + "-" + algorithmID + "-" + instanceInfo.getInstanceID() + "-" + configID + "-" + runID + ".xml";
	                taskQueue.add(new SingleAlgorithmExperimentTask(_experimentName, experimentID, problemID, instanceID, algorithmID, config.getAlgorithmParams(), runID, _timeoutS, zos));
	            }
	        }
	        new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(taskQueue.toArray(new Runnable[]{}));
		}
	}

	@Override
	protected long getEstimatedTime()
	{		
		return _timeoutS*1000;
	}

	@Override
	protected long getNumberOfConfigs()
	{		
		return _numConfigs;
	}
}
