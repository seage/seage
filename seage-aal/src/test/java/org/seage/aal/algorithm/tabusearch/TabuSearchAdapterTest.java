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

package org.seage.aal.algorithm.tabusearch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.aal.problem.TestPhenotypeEvaluator;

/**
 * TabuSearchAdapterTest.
 * @author Richard Malek
 */
@Testable
public class TabuSearchAdapterTest extends AlgorithmAdapterTestBase<TestSolution> {

  public TabuSearchAdapterTest() throws Exception {
    super();
  }

  @BeforeEach
  public void initAlgorithm() throws Exception {
    algAdapter = new TabuSearchAdapter<TestPhenotype, TestSolution>(
        new TestMoveManager(), 
        new TestObjectiveFunction(),
        new TestPhenotypeEvaluator()) {

      @Override
      public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception {
        this.solutions = new TestSolution[source.length];

        for (int i = 0; i < source.length; i++) {
          TestSolution s = new TestSolution(source[i].getSolution());
          this.solutions[i] = s;
        }
      }

      @Override
      public TestPhenotype[] solutionsToPhenotype() throws Exception {
        TestPhenotype[] result = new TestPhenotype[this.solutions.length];
        for (int i = 0; i < this.solutions.length; i++) {
          result[i] = solutionToPhenotype(this.solutions[i]);
        }
        return result;
      }

      @Override
      public TestPhenotype solutionToPhenotype(TestSolution solution) throws Exception {
        return new TestPhenotype((Integer[]) solution.solution);
      }
    };

    algParams = new AlgorithmParams();
    algParams.putValue("numIterDivers", 1);
    algParams.putValue("iterationCount", 3);
    algParams.putValue("numSolutions", 1);
    algParams.putValue("tabuListLength", 1);
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
    params.putValue("numIterDivers", 0);
    params.putValue("iterationCount", 0);
    params.putValue("numSolutions", 0);
    params.putValue("tabuListLength", 0);
    super.setAlgParameters(params);
    super.testAlgorithmWithParamsAtZero();
  }

  @Test
  @Override
  public void testAsyncRunning() throws Exception {
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
  public void testReport() throws Exception {
    super.testReport();
  }

  @Test
  @Override
  public void testAlgorithmWithParamsNull() throws Exception {
    super.testAlgorithmWithParamsNull();
  }
}
