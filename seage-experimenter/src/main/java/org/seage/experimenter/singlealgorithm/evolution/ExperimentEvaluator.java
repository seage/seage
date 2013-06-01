package org.seage.experimenter.singlealgorithm.evolution;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import org.seage.aal.data.ProblemConfig;
import org.seage.experimenter.singlealgorithm.SingleAlgorithmExperiment;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.thread.TaskRunnerEx;

public class ExperimentEvaluator extends SubjectEvaluator<Double>
{
	@Override
	public void evaluateSubjects(List<Subject<Double>> subjects) throws Exception 
	{
//		List<Runnable> taskQueue = new ArrayList<Runnable>();
		
		for(Subject<Double> s : subjects)
		{
			ExperimentSubject es = (ExperimentSubject)s;
			
			double val = 0;
			for(int i=0;i<es.getChromosome().getLength();i++)
				val+=(i+1)*es.getChromosome().getGene(i);
			
			es.setObjectiveValue(new double[]{val});
		}
		
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
	protected double[] evaluate(Subject<Double> solution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
