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

package org.seage.aal.algorithm.sannealing;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.*;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;

/**
 *
 * @author rick
 */
public class SimulatedAnnealingAdapterTest extends AlgorithmAdapterTestBase{
    
    public SimulatedAnnealingAdapterTest() throws Exception
    {
        super();
    }

    @Before
    public void initAlgorithm() throws Exception
    {
        _algorithm = new TestSimulatedAnnealingAdapter(null, new TestObjectiveFunction(), new TestMoveManager(), false, "");
        
        _algParams = new AlgorithmParams("SimulatedAnnealingTest");
        _algParams.putValue("problemID", "SimulatedAnnealingTest");
        _algParams.putValue("instance", "TestInstance");
        
        DataNode params = new DataNode("Parameters");
        params.putValue("annealCoeficient", 0.1);
        params.putValue("maxInnerIterations", 10);
        params.putValue("maxTemperature", 100);
        params.putValue("minTemperature", 1);
        params.putValue("numInnerSuccesses", 1);
        params.putValue("numSolutions", 1);
        
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
        params.putValue("annealCoeficient", 0);
        params.putValue("maxInnerIterations", 0);
        params.putValue("maxTemperature", 0);
        params.putValue("minTemperature", 0);
        params.putValue("numInnerSuccesses", 0);
        params.putValue("numSolutions", 0);
        _tester.setAlgParameters(params);
        _tester.testAlgorithmWithParamsAtZero();
    }
    
    @Test
    @Override    
    public void testAsyncRunning() throws Exception
    {
        DataNode params = new DataNode("Parameters");
        params.putValue("annealCoeficient", 0.999);
        params.putValue("maxInnerIterations", 1000000);
        params.putValue("maxTemperature", 100000);
        params.putValue("minTemperature", 1);
        params.putValue("numInnerSuccesses", 100000);
        params.putValue("numSolutions", 1);
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
