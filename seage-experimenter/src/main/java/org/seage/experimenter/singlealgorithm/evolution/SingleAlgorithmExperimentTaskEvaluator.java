package org.seage.experimenter.singlealgorithm.evolution;

import java.util.ArrayList;
import java.util.List;

import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperimentTask;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.thread.TaskRunnerEx;

public class SingleAlgorithmExperimentTaskEvaluator extends SubjectEvaluator<SingleAlgorithmExperimentTaskSubject>
{
	@Override
	public void evaluateSubjects(List<SingleAlgorithmExperimentTaskSubject> subjects) throws Exception 
	{
		List<Runnable> taskQueue = new ArrayList<Runnable>();
		for(SingleAlgorithmExperimentTaskSubject s : subjects)
		{	
			double val = 0;
												
			//taskQueue.add( new SingleAlgorithmExperimentTask(experimentType, experimentID, timeoutS, config, reportName, reportOutputStream, runID));
			
			s.setObjectiveValue(new double[]{val});
		}
		
		new TaskRunnerEx(Runtime.getRuntime().availableProcessors()).run(taskQueue.toArray(new Runnable[]{}));

		
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
