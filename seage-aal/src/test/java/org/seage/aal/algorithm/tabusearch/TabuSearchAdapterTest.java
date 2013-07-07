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

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.algorithm.AlgorithmParams;

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
        _algorithm = new TabuSearchAdapter(new TestMoveManager(), new TestObjectiveFunction(), "")
        {
            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _solutions = new TestSolution[source.length];
                
                for(int i=0;i<source.length;i++)
                {
                    _solutions[i] = new TestSolution(source[i]);
                }
                
            }
            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                List<Object[]> result = new ArrayList<Object[]>();
                
                for(int i=0;i<_solutions.length;i++)
                {
                    result.add(((TestSolution)_solutions[i]).solution);
                }
                return (Object[][]) result.toArray(new Object[][]{});
            }
        };
        
        _algParams = new AlgorithmParams();      
        
        _algParams.putValue("numIterDivers", 1);
        _algParams.putValue("numIteration", 3);
        _algParams.putValue("numSolutions", 1);
        _algParams.putValue("tabuListLength", 1);
        
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
    	AlgorithmParams params = new AlgorithmParams();
        params.putValue("numIterDivers", 0);
        params.putValue("numIteration", 0);
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
        params.putValue("numIteration", 1000000);
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
