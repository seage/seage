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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.algbase.AlgorithmAdapterTestBase;

/**
 *
 * @author Richard Malek
 */
public class SimulatedAnnealingAdapterTest extends AlgorithmAdapterTestBase<TestSolution>
{

    public SimulatedAnnealingAdapterTest() throws Exception
    {
        super();
    }

    @BeforeEach
    public void initAlgorithm() throws Exception
    {
        _algAdapter = new TestSimulatedAnnealingAdapter(null, new TestObjectiveFunction(), new TestMoveManager(), false,
                "");

        _algParams = new AlgorithmParams();

        //_algParams.putValue("annealCoeficient", 0.1);
        _algParams.putValue("numIterations", 10);
        _algParams.putValue("maxTemperature", 100);
        _algParams.putValue("minTemperature", 1);

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
        params.putValue("numIterations", 0);
        params.putValue("maxTemperature", 0);
        params.putValue("minTemperature", 0);
        _tester.setAlgParameters(params);
        _tester.testAlgorithmWithParamsAtZero();
    }

    @Test
    @Override
    public void testAsyncRunning() throws Exception
    {
        AlgorithmParams params = new AlgorithmParams();
        params.putValue("numIterations", 1000000);
        params.putValue("maxTemperature", 100000);
        params.putValue("minTemperature", 1);
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
