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

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;
import org.seage.metaheuristic.genetics.Evaluator;
import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.Subject;

/**
 *
 * @author rick
 */
public class GeneticAlgorithmAdapterTest extends AlgorithmAdapterTestBase{
    
    public GeneticAlgorithmAdapterTest() throws Exception
    {
        super();
    }

    @Before
    public void initAlgorithm() throws Exception
    {
        _algorithm = new GeneticAlgorithmAdapter(new GeneticOperator(), new Evaluator()
        {            
            @Override
            public double[] evaluate(Subject solution) throws Exception
            {
            	double val = 0;
            	for(int i=0;i<solution.getGenome().getChromosome(0).getGeneArray().length-1;i++)
            	{
            		val += Math.abs(solution.getGenome().getChromosome(0).getGene(i).getValue() - solution.getGenome().getChromosome(0).getGene(i+1).getValue());
            	}
            	
                return new double[]{val};
            }
        }, true, "");    
        
        _algParams = new AlgorithmParams("GeneticAlgorithmTest");
        _algParams.putValue("problemID", "GeneticAlgorithmTest");
        _algParams.putValue("instance", "TestInstance");
        
        DataNode params = new DataNode("Parameters");
        params.putValue("crossLengthPct", 0);
        params.putValue("mutateLengthPct", 0);
        params.putValue("eliteSubjectPct", 0);
        params.putValue("iterationCount", 1);
        params.putValue("mutateSubjectPct", 0);
        params.putValue("numSolutions", 1);
        params.putValue("randomSubjectPct", 0); 
        
        _algParams.putDataNodeRef(params);
        
        _tester = new AlgorithmAdapterTester(_algorithm, _solutions, _algParams);
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
}
