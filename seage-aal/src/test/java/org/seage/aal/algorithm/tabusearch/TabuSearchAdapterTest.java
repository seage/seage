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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.TestPhenotype;

/**
 *
 * @author Richard Malek
 */
public class TabuSearchAdapterTest extends AlgorithmAdapterTestBase<TestSolution>
{

    public TabuSearchAdapterTest() throws Exception
    {
        super();
    }

    @BeforeEach
    public void initAlgorithm() throws Exception
    {
        _algAdapter = new TabuSearchAdapter<TestPhenotype, TestSolution>(new TestMoveManager(), new TestObjectiveFunction(), null)
        {
            private TestSolution[] _solutions0;

            @Override
            public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception
            {
                _solutions0 = new TestSolution[source.length];
                _solutions = new TestSolution[source.length];

                for (int i = 0; i < source.length; i++)
                {
                    TestSolution s = new TestSolution(source[i].getSolution());
                    _solutions0[i] = s;
                    _solutions[i] = s;
                }

            }

            @Override
            public TestPhenotype[] solutionsToPhenotype() throws Exception
            {
                assertEquals(_solutions0.length, _solutions.length);
                assertNotSame(_solutions0[0], _solutions[0]);
                for (int i = 1; i < _solutions.length; i++)
                {
                    assertSame(_solutions0[i], _solutions[i]);
                }
                return null;
            }

			@Override
			public TestPhenotype solutionToPhenotype(TestSolution solution) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
        };

        _algParams = new AlgorithmParams();
        _algParams.putValue("numIterDivers", 1);
        _algParams.putValue("iterationCount", 3);
        _algParams.putValue("numSolutions", 1);
        _algParams.putValue("tabuListLength", 1);
    }

    @Override
    @Test
    public void testAlgorithm() throws Exception
    {
        super.testAlgorithm();
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
        super.setAlgParameters(params);
        super.testAlgorithmWithParamsAtZero();
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
        super.setAlgParameters(params);
        super.testAsyncRunning();
    }

    @Test
    @Override
    public void testReport() throws Exception
    {
    	super.testReport();
    }

    @Test
    @Override
    public void testAlgorithmWithParamsNull() throws Exception
    {
    	super.testAlgorithmWithParamsNull();
    }
}
