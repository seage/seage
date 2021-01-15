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

package org.seage.aal.algorithm.fireflies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.problem.TestPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
//@Ignore("Adapter class not fully implemented yet")
public class FireflyAlgorithmAdapterTest extends AlgorithmAdapterTestBase<TestSolution> {

  public FireflyAlgorithmAdapterTest() throws Exception {
    super();
  }

  @BeforeEach
  public void initAlgorithm() throws Exception {
    this.algAdapter = new TestFireflyAlgorithmAdapter(new TestOperator(), new TestObjectiveFunction(),
        new TestPhenotypeEvaluator(), false);

    algParams = new AlgorithmParams();

    algParams.putValue("iterationCount", 10);
    algParams.putValue("numSolutions", NUM_SOLUTIONS);
    algParams.putValue("timeStep", 1);
    algParams.putValue("withDecreasingRandomness", 1);
    algParams.putValue("absorption", 1);
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
    params.putValue("numSolutions", 0);
    params.putValue("timeStep", 0);
    params.putValue("withDecreasingRandomness", 0);
    params.putValue("absorption", 0);
    super.setAlgParameters(params);
    super.testAlgorithmWithParamsAtZero();
  }

  @Test
  @Override
  public void testAsyncRunning() throws Exception {
    AlgorithmParams params = new AlgorithmParams();
    params.putValue("iterationCount", 10000000);
    params.putValue("numSolutions", 5);
    params.putValue("timeStep", 1);
    params.putValue("withDecreasingRandomness", 1);
    params.putValue("absorption", 0);
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
