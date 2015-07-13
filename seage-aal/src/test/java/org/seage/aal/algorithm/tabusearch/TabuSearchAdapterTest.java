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

package org.seage.aal.algorithm.tabusearch;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.algbase.AlgorithmAdapterTestBase;

/**
 *
 * @author rick
 */
public class TabuSearchAdapterTest extends AlgorithmAdapterTestBase{
    
    public TabuSearchAdapterTest() throws Exception
    {
        super();
    }

    @Before
    public void initAlgorithm() throws Exception
    {
        _algAdapter = new TabuSearchAdapter(new TestMoveManager(), new TestObjectiveFunction(), "")
        {
        	private TestSolution[] _solutions0;
            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
            	_solutions0 = new TestSolution[source.length];
                _solutions = new TestSolution[source.length];
                
                for(int i=0;i<source.length;i++)
                {
                	TestSolution s = new TestSolution(source[i]);
                	_solutions0[i] = s;
                    _solutions[i] = s;
                }
                
            }
            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
//                List<Object[]> result = new ArrayList<Object[]>();
//                
//                for(int i=0;i<_solutions.length;i++)
//                {
//                    result.add(((TestSolution)_solutions[i]).solution);
//                }
            	Assert.assertEquals(_solutions0.length, _solutions.length);
            	Assert.assertNotSame(_solutions0[0], _solutions[0]);
            	for(int i=1;i<_solutions.length;i++)
            	{
            		Assert.assertSame(_solutions0[i], _solutions[i]);
            	}
                return null;//(Object[][]) result.toArray(new Object[][]{});
            }
        };
        
        _algParams = new AlgorithmParams();      
        
        _algParams.putValue("numIterDivers", 1);
        _algParams.putValue("iterationCount", 3);
        _algParams.putValue("numSolutions", 1);
        _algParams.putValue("tabuListLength", 1);
        
        _tester = new AlgorithmAdapterTester(_algAdapter, /*_solutions,*/ _algParams);
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
    	AlgorithmParams params = new AlgorithmParams();
        params.putValue("numIterDivers", 0);
        params.putValue("iterationCount", 0);
        params.putValue("numSolutions", 0);
        params.putValue("tabuListLength", 0);
        _tester.setAlgParameters(params);
        
        _tester.testAlgorithmWithParamsAtZero();
    }
    
    @Test
    @Override    
    public void testAsyncRunning() throws Exception
    {
    	AlgorithmParams params = new AlgorithmParams();
        params.putValue("numIterDivers", 1);
        params.putValue("iterationCount", 1000000);
        params.putValue("numSolutions", 1);
        params.putValue("tabuListLength", 1);
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
