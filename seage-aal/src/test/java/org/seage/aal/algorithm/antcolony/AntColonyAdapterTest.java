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

package org.seage.aal.algorithm.antcolony;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.algbase.AlgorithmAdapterTestBase;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author rick
 */
public class AntColonyAdapterTest extends AlgorithmAdapterTestBase{
    
    public AntColonyAdapterTest() throws Exception
    {
        super();
    }

    @Before
    public void initAlgorithm() throws Exception
    {    	
    	Graph graph = new TestGraph();
    	for(int i=0;i<SOLUTION_LENGTH;i++)
    	{
    		Node n1 = new Node(i+1);
    		graph.getNodes().put(i+1, n1);
    		
    	}
    	    	
        _algAdapter = new TestAntColonyAdapter(new AntBrain(graph), graph);
        _algParams = new AlgorithmParams();

        _algParams.putValue("iterationCount", 3);
        _algParams.putValue("alpha", 100);
        _algParams.putValue("beta", 1);
        _algParams.putValue("defaultPheromone", 1);
        _algParams.putValue("qantumOfPheromone", 1);
        _algParams.putValue("localEvaporation", 1);
        
        _tester = new AlgorithmAdapterTester(_algAdapter, /*_solutions,*/ _algParams);
    }  
    
    @Test
    @Override    
    public void testAlgorithm() throws Exception
    {
        _tester.testAlgorithm();
    }

    @Test
    @Override    
    public void testAlgorithmWithParamsAtZero() throws Exception
    {
    	AlgorithmParams params = new AlgorithmParams();
        //params.putValue("numAnts", 0);
        params.putValue("iterationCount", 0);
        params.putValue("alpha", 0);
        params.putValue("beta", 0);
        params.putValue("defaultPheromone", 0);
        params.putValue("qantumOfPheromone", 0);
        params.putValue("localEvaporation", 0);
    	_tester.setAlgParameters(params);
        _tester.testAlgorithmWithParamsAtZero();
    }

    @Test
    @Override
    public void testAsyncRunning() throws Exception
    {
    	AlgorithmParams params = new AlgorithmParams();
        //params.putValue("numAnts", 0.1);
        params.putValue("iterationCount", 1000000);
        params.putValue("alpha", 100);
        params.putValue("beta", 1);
        params.putValue("defaultPheromone", 1);
        params.putValue("qantumOfPheromone", 1);
        params.putValue("localEvaporation", 1);
        
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
