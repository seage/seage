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
package org.seage.problem.jssp.genetics;

import org.seage.metaheuristic.genetics.Chromosome;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.problem.jssp.ScheduleManager;


/**
 * Summary description for JSSPGSEvaluator.
 */
public class JsspEvaluator extends SubjectEvaluator
{
	private ScheduleManager _scheduleManager;
	public JsspEvaluator(ScheduleManager scheduleManager)
	{
		_scheduleManager = scheduleManager;
	}

	public double[] evaluate(Subject subject) throws Exception
	{
		Chromosome chrom = subject.getChromosome();
		int[] jobArray = new int[chrom.getLength()];

		for (int i = 0; i < jobArray.length; i++)
			jobArray[i] = (Integer) chrom.getGene(i);

		Object[] eval = _scheduleManager.evaluateSchedule(jobArray, false);

		int makespan = ((Integer)eval[0]).intValue();
		
		return new double[] { makespan };
	}
}

