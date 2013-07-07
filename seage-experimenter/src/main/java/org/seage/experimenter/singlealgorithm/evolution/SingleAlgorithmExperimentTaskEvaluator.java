package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimentTask;
import org.seage.metaheuristic.genetics.SubjectEvaluator;

public class SingleAlgorithmExperimentTaskEvaluator extends SubjectEvaluator<SingleAlgorithmExperimentTaskSubject>
{
	private long _experimentID;	
	private String _problemID;
	private String _instanceID;
	private String _algorithmID;
	private long _timeoutS;
	private ZipOutputStream _reportOutputStream;
	

	public SingleAlgorithmExperimentTaskEvaluator(long experimentID, String problemID, String instanceID, String algorithmID, long timeoutS, ZipOutputStream reportOutputStream)
	{
		super();
		_experimentID = experimentID;
		_problemID = problemID;
		_instanceID = instanceID;
		_algorithmID = algorithmID;
		_timeoutS = timeoutS;
		_reportOutputStream = reportOutputStream;
	}

	@Override
	public void evaluateSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception 
	{
		List<Runnable> taskQueue = new ArrayList<Runnable>();
		for(SingleAlgorithmExperimentTaskSubject s : subjects)
		{	
			double val = 0;
												
			AlgorithmParams algorithmParams = null;
			taskQueue.add( new SingleAlgorithmExperimentTask("SingleAlgorithmExperiment", _experimentID, _problemID, _instanceID, _algorithmID, algorithmParams, 1, _timeoutS, _reportOutputStream));
			
			s.setObjectiveValue(new double[]{val});
		}
		
		//new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(taskQueue.toArray(new Runnable[]{}));

		
//		String reportPath = String.format("output/experiment-logs/%s-%s-%s-%s.zip", experimentID, problemID, algorithmID, instanceID);
//
//        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(reportPath)));
//
//        // Create a task queue
//        
//        for(ProblemConfig config : _configurator.prepareConfigs(pi, algorithmID, instanceInfo, numOfConfigs))
//        {
//        	String configID = config.getConfigID();
//
//            for (int runID = 1; runID <= 5; runID++)
//            {
//                String reportName = problemID + "-" + algorithmID + "-" + instanceID + "-" + configID + "-" + runID + ".xml";
//                taskQueue.add(new SingleAlgorithmExperiment(_experimentName, experimentID, timeoutS, config, reportName, zos, runID));
//            }
//        }
//        new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(taskQueue.toArray(new Runnable[]{}));
//
//        zos.close();         
	}

	@Override
	protected double[] evaluate(SingleAlgorithmExperimentTaskSubject solution) throws Exception {
		throw new Exception("Should be unimplemented");
	}

}
