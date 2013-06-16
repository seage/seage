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

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.algorithm.genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;
import org.seage.metaheuristic.genetics.BasicGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;

/**
 *
 * @author rick
 */
public class GeneticAlgorithmAdapterTest extends AlgorithmAdapterTestBase{
    
    public GeneticAlgorithmAdapterTest() throws Exception
    {
        super();
    }
//    {            
//        @Override
//        public double[] evaluate(Subject<?> solution) throws Exception
//        {
//        	double val = 0;
//        	for(int i=0;i<solution.getChromosome().getGenes().length-1;i++)
//        	{
//        		val += Math.abs((Integer)solution.getChromosome().getGene(i) - (Integer)solution.getChromosome().getGene(i+1));
//        	}
//        	
//            return new double[]{val};
//        }
//    }
    @Before
    public void initAlgorithm() throws Exception
    {
    	SubjectEvaluator<Subject<Integer>> se = new SubjectEvaluator<Subject<Integer>>()
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
        
		_algorithm = new GeneticAlgorithmAdapter<Subject<Integer>>(new BasicGeneticOperator<Subject<Integer>, Integer>(), se, false, "") 
        {			
			@Override
			public Object[][] solutionsToPhenotype() throws Exception 
			{
				_evaluator.evaluateSubjects(_solutions);
				Collections.sort(_solutions, _comparator);
		
				Object[][] result = new Object[_solutions.size()][];
		
				for (int i = 0; i < _solutions.size(); i++)
				{
					int numGenes = _solutions.get(i).getChromosome().getLength();
					result[i] = Arrays.copyOf(_solutions.get(i).getChromosome().getGenes(), numGenes);
				}
				return result;
			}
			
			@Override
			public void solutionsFromPhenotype(Object[][] source) throws Exception 
			{
				_solutions = new ArrayList<Subject<Integer>>(source.length);
				for (int i = 0; i < source.length; i++)				
					_solutions.add( new Subject<Integer>((Integer[]) source[i]));								
			}
		};
        
        _algParams = new AlgorithmParams("GeneticAlgorithmTest");
        _algParams.putValue("problemID", "GeneticAlgorithmTest");
        _algParams.putValue("instance", "TestInstance");
        
        DataNode params = new DataNode("Parameters");
        params.putValue("crossLengthPct", 40);
        params.putValue("mutateLengthPct", 20);
        params.putValue("eliteSubjectPct", 1);
        params.putValue("iterationCount", 10);
        params.putValue("mutateSubjectPct", 10);
        params.putValue("numSolutions", 10);
        params.putValue("randomSubjectPct", 1); 
        
        _algParams.putDataNodeRef(params);
        
        _tester = new AlgorithmAdapterTester(_algorithm, /*_solutions,*/ _algParams);
    }
    
    @Override
    @Test
    public void testPhenotype() throws Exception
    {
        _tester.testPhenotype();
        
    }
    
    @Override
    @Test
    public void testAlgorithm() throws Exception
    {
        _tester.testAlgorithm();
    }

    @Override
    @Test
    public void testAlgorithmWithParamsAtZero() throws Exception
    {
        DataNode params = new DataNode("Parameters");
        params.putValue("crossLengthPct", 0);
        params.putValue("mutateLengthPct", 0);
        params.putValue("eliteSubjectPct", 0);
        params.putValue("iterationCount", 0);
        params.putValue("mutateSubjectPct", 0);
        params.putValue("numSolutions", 0);
        params.putValue("randomSubjectPct", 0); 
        _tester.setAlgParameters(params);
        _tester.testAlgorithmWithParamsAtZero();
    }
    
    @Test
    @Override    
    public void testAsyncRunning() throws Exception
    {
        DataNode params = new DataNode("Parameters");
        params.putValue("crossLengthPct", 1);
        params.putValue("mutateLengthPct", 1);
        params.putValue("eliteSubjectPct", 1);
        params.putValue("iterationCount", 1000000);
        params.putValue("mutateSubjectPct", 1);
        params.putValue("numSolutions", 10);
        params.putValue("randomSubjectPct", 1); 
        _tester.setAlgParameters(params);
        _tester.testAsyncRunning();        
    }
    
    @Test
	@Override
	public void testReport() throws Exception
	{
		_tester.testReport();
	}
    
    @Test
    @Override
	public void testAlgorithmWithParamsNull() throws Exception
	{
		_tester.testAlgorithmWithParamsNull();		
	}
}
