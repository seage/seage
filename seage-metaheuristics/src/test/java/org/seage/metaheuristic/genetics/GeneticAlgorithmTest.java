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
package org.seage.metaheuristic.genetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.seage.metaheuristic.IAlgorithmListener;

public class GeneticAlgorithmTest {

	@Test
	public void testAlgorithm() throws Exception 
	{		
		SubjectEvaluator<Integer> evaluator = new SubjectEvaluator<Integer>()
		{

			@Override
			protected double[] evaluate(Subject<Integer> solution) throws Exception
			{
				double val = 0;
	        	for(int i=0;i<solution.getChromosome().getGenes().length;i++)
	        	{
	        		val += i*solution.getChromosome().getGene(i);
	        	}
	        	
	            return new double[]{val};
			}
		};
		GeneticSearch<Integer> ga = new GeneticSearch<Integer>(new GeneticOperator<Integer>(), evaluator );
		ga.addGeneticSearchListener(new Listener());
		ga.setCrossLengthPct(0.4);
		ga.setEliteSubjectsPct(0.1);
		ga.setIterationToGo(1000);
		ga.setMutateChromosomeLengthPct(0.2);
		ga.setMutatePopulationPct(0.2);
		ga.setPopulationCount(100);
		ga.setRandomSubjectsPct(0.1);
		
		List<Subject<Integer>> subjects = new ArrayList<Subject<Integer>>();
		
		Random rnd = new Random(4);
		for(int i=0;i<100;i++)
		{
			Integer[] arr = new Integer[100];
			for(int j=0;j<100;j++)
			{
				arr[j] = rnd.nextInt(100);
				
			}
			subjects.add(new Subject<Integer>(arr));
		}
		
		ga.startSearching(subjects);
	}
	
	private class Listener implements IAlgorithmListener<GeneticSearchEvent<Integer>>
	{

		@Override
		public void algorithmStarted(GeneticSearchEvent<Integer> e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void algorithmStopped(GeneticSearchEvent<Integer> e)
		{
			System.out.println(e.getGeneticSearch().getBestSubject().getChromosome());
			
		}

		@Override
		public void newBestSolutionFound(GeneticSearchEvent<Integer> e)
		{
			System.out.println(e.getGeneticSearch().getBestSubject().getFitness()[0]);
			
		}

		@Override
		public void iterationPerformed(GeneticSearchEvent<Integer> e)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void noChangeInValueIterationMade(GeneticSearchEvent<Integer> e)
		{
			// TODO Auto-generated method stub
			
		}

		
		
	}
} 
