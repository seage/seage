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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.aal.algorithm.sannealing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.problem.TestPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
public class SimulatedAnnealingAdapterTest extends AlgorithmAdapterTestBase<TestSolution> {

  public SimulatedAnnealingAdapterTest() throws Exception {
    super();
  }

  @BeforeEach
  public void initAlgorithm() throws Exception {
    algAdapter = new TestSimulatedAnnealingAdapter(null, new TestObjectiveFunction(), new TestMoveManager(),
        new TestPhenotypeEvaluator(), false);
    algParams = new AlgorithmParams();

    // _algParams.putValue("annealCoeficient", 0.1);
    algParams.putValue("iterationCount", 10);
    algParams.putValue("maxTemperature", 100);
    algParams.putValue("minTemperature", 1);
    algParams.putValue("annealingCoefficient", 0.995);
  }

  @Override
  @Test
  public void testAlgorithm() throws Exception {
    super.testAlgorithm();
  }

  @Override
  @Test
  public void testAlgorithmWithParamsAtZero() throws Exception {
    AlgorithmParams params = new AlgorithmParams();
    params.putValue("iterationCount", 0);
    params.putValue("maxTemperature", 0);
    params.putValue("minTemperature", 0);
    params.putValue("annealingCoefficient", 0);
    super.setAlgParameters(params);
    super.testAlgorithmWithParamsAtZero();
  }

  @Test
  @Override
  public void testAsyncRunning() throws Exception {
    AlgorithmParams params = new AlgorithmParams();
    params.putValue("iterationCount", 1000000);
    params.putValue("maxTemperature", 100000);
    params.putValue("minTemperature", 1);
    params.putValue("annealingCoefficient", 0.995);
    super.setAlgParameters(params);
    super.testAsyncRunning();
  }

  @Test
  @Override
  public void testReport() throws Exception {
    super.testReport();
  }

  @Test
  @Override
  public void testAlgorithmWithParamsNull() throws Exception {
    super.testAlgorithmWithParamsNull();
  }
}
