package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimentTask;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.thread.TaskRunnerEx;

public class SingleAlgorithmExperimentTaskEvaluator extends SubjectEvaluator<SingleAlgorithmExperimentTaskSubject>
{
	private long _experimentID;	
	private String _problemID;
	private String _instanceID;
	private String _algorithmID;
	private long _timeoutS;
	private ZipOutputStream _reportOutputStream;
	private int iterationID = 1;

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
		List<SingleAlgorithmExperimentTask> taskQueue = new ArrayList<SingleAlgorithmExperimentTask>();
		HashMap<SingleAlgorithmExperimentTask, SingleAlgorithmExperimentTaskSubject> taskMap = new HashMap<SingleAlgorithmExperimentTask, SingleAlgorithmExperimentTaskSubject>();
		
		for(SingleAlgorithmExperimentTaskSubject s : subjects)
		{	
			AlgorithmParams algorithmParams = new AlgorithmParams(); // subject
			for(int i=0;i<s.getChromosome().getLength();i++)
			{
				algorithmParams.putValue(s.getParamNames()[i], s.getChromosome().getGene(i));
			}
			
			SingleAlgorithmExperimentTask task = new SingleAlgorithmExperimentTask("SingleAlgorithmEvolution", _experimentID, _problemID, _instanceID, _algorithmID, algorithmParams, iterationID, _timeoutS, _reportOutputStream); 
			
			taskMap.put(task, s);
			taskQueue.add(task);						
		}
		
		new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(taskQueue.toArray(new Runnable[]{}));

		for(SingleAlgorithmExperimentTask task : taskQueue)
		{
			SingleAlgorithmExperimentTaskSubject s = taskMap.get(task);
			double val = task.getExperimentTaskReport().getDataNode("AlgorithmReport").getDataNode("Statistics").getValueDouble("bestObjVal");
			s.setObjectiveValue(new double[]{val});
		}
		
		iterationID++;
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
