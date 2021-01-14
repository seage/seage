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

package org.seage.aal.algorithm.genetics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.aal.problem.TestPhenotypeEvaluator;
import org.seage.metaheuristic.genetics.BasicGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;

/**
 *
 * @author Richard Malek
 */
class GeneticAlgorithmAdapterTest extends AlgorithmAdapterTestBase<Subject<Integer>> {

  public GeneticAlgorithmAdapterTest() throws Exception {
    super();
  }

  @BeforeEach
  public void initAlgorithm() throws Exception {
    SubjectEvaluator<Subject<Integer>> se = new SubjectEvaluator<Subject<Integer>>() {
      @Override
      protected double[] evaluate(Subject<Integer> solution) throws Exception {
        double val = 0;
        for (int i = 0; i < solution.getChromosome().getGenes().length; i++) {
          val += i * solution.getChromosome().getGene(i);
        }

        return new double[] { val };
      }
    };

    algAdapter = new GeneticAlgorithmAdapter<TestPhenotype, Subject<Integer>>(
        new BasicGeneticOperator<Subject<Integer>, Integer>(), se, new TestPhenotypeEvaluator(), false) {

      @Override
      public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception {
        this.solutions = new ArrayList<Subject<Integer>>(source.length);
        for (int i = 0; i < source.length; i++) {
          Subject<Integer> s = new Subject<Integer>(source[i].getSolution());
          this.solutions.add(s);
        }
      }

      @Override
      public TestPhenotype[] solutionsToPhenotype() throws Exception {
        TestPhenotype[] result = new TestPhenotype[this.solutions.size()];
        for (int i = 0; i < this.solutions.size(); i++) {
          result[i] = solutionToPhenotype(this.solutions.get(i));
        }
        return result;
      }

      @Override
      public TestPhenotype solutionToPhenotype(Subject<Integer> solution) throws Exception {
        return new TestPhenotype(solution.getChromosome().getGenes());
      }
    };

    algParams = new AlgorithmParams();
    algParams.putValue("crossLengthPct", 40);
    algParams.putValue("mutateLengthPct", 20);
    algParams.putValue("eliteSubjectPct", 1);
    algParams.putValue("iterationCount", 10);
    algParams.putValue("mutateSubjectPct", 10);
    algParams.putValue("numSolutions", NUM_SOLUTIONS);
    algParams.putValue("randomSubjectPct", 1);

    // _tester = new AlgorithmAdapterTestImpl<>(_algAdapter, /*this.solutions,*/
    // _algParams);
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
    params.putValue("crossLengthPct", 0);
    params.putValue("mutateLengthPct", 0);
    params.putValue("eliteSubjectPct", 0);
    params.putValue("iterationCount", 0);
    params.putValue("mutateSubjectPct", 0);
    params.putValue("numSolutions", 0);
    params.putValue("randomSubjectPct", 0);
    super.setAlgParameters(params);
    super.testAlgorithmWithParamsAtZero();
  }

  @Test
  @Override
  public void testAsyncRunning() throws Exception {
    AlgorithmParams params = new AlgorithmParams();
    params.putValue("crossLengthPct", 1);
    params.putValue("mutateLengthPct", 1);
    params.putValue("eliteSubjectPct", 1);
    params.putValue("iterationCount", 1000000);
    params.putValue("mutateSubjectPct", 1);
    params.putValue("numSolutions", 10);
    params.putValue("randomSubjectPct", 1);
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
