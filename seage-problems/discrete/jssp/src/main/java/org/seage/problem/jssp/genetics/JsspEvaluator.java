package org.seage.problem.jssp.genetics;

import jssp.data.*;

import ailibrary.data.*;
import ailibrary.algorithm.*;
import ailibrary.algorithm.genetics.*;


/**
 * Summary description for JSSPGSEvaluator.
 */
public class JsspEvaluator implements Evaluator
{
	private ScheduleManager _scheduleManager;
	public JsspEvaluator(ScheduleManager scheduleManager)
	{
		_scheduleManager = scheduleManager;
	}

	public double[] evaluate(Subject subject) throws Exception
	{
		Chromosome chrom = subject.getGenome().getChromosome(0);
		int[] jobArray = new int[chrom.getLength()];

		for (int i = 0; i < jobArray.length; i++)
			jobArray[i] = chrom.getGene(i).getValue();

		Object[] eval = _scheduleManager.evaluateSchedule(jobArray, false);

		int makespan = ((Integer)eval[0]).intValue();
		
		return new double[] { makespan };
	}
}

