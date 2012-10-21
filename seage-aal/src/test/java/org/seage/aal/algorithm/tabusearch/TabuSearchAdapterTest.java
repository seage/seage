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

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.data.AlgorithmParams;
import org.seage.data.DataNode;

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
        _algorithm = new TestTabuSearchAdapter(new TestMoveManager()
        , new TestObjectiveFunction()
        , new TestLongTermMemory()
        , "");
        
        _algParams = new AlgorithmParams("TabuSearchTest");
        _algParams.putValue("problemID", "TabuSearchTest");
        _algParams.putValue("instance", "TestInstance");
        
        DataNode params = new DataNode("Parameters");
        params.putValue("numIterDivers", 1);
        params.putValue("numIteration", 1);
        params.putValue("numSolutions", 1);
        params.putValue("tabuListLength", 1);
        
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
    public void testAlgorithmWithZeroParams() throws Exception
    {
        _algParams = new AlgorithmParams("TabuSearchTest");
        _algParams.putValue("problemID", "TabuSearchTest");
        _algParams.putValue("instance", "TestInstance");
        
        DataNode params = new DataNode("Parameters");
        params.putValue("numIterDivers", 0);
        params.putValue("numIteration", 0);
        params.putValue("numSolutions", 0);
        params.putValue("tabuListLength", 0);
        
        _algParams.putDataNodeRef(params);
        
        _tester = new AlgorithmAdapterTester(_algorithm, _solutions, _algParams);        
        _tester.testAlgorithmWithZeroParams();
    }
}
