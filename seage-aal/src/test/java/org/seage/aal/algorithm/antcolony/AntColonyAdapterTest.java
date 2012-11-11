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
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
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
    	Graph graph = new Graph();
    	Node n1 = new Node(1);
    	Node n2 = new Node(2);
    	Edge e = new Edge(n1, n2);
    	e.setEdgePrice(1);
    	graph.getNodes().put(1, n1);
    	graph.getNodes().put(2, n2);
    	graph.getEdges().add(e);
    	
        _algorithm = new TestAntColonyAdapter(new AntBrain(), graph);
        _algParams = new AlgorithmParams("AntColonyTest");
        _algParams.putValue("problemID", "AntColonyTest");
        _algParams.putValue("instance", "TestInstance");
        
        DataNode params = new DataNode("Parameters");
        //params.putValue("numAnts", 0.1);
        params.putValue("iterationCount", 2);
        params.putValue("alpha", 100);
        params.putValue("beta", 1);
        params.putValue("defaultPheromone", 1);
        params.putValue("qantumOfPheromone", 1);
        params.putValue("localEvaporation", 1);
        
        _algParams.putDataNodeRef(params);
        
        _tester = new AlgorithmAdapterTester(_algorithm, _solutions, _algParams);
    }
    
    @Test
    @Override    
    public void testPhenotype() throws Exception
    {
        _tester.testPhenotype();
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
    	DataNode params = new DataNode("Parameters");
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
    	DataNode params = new DataNode("Parameters");
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
}
