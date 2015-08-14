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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.seage.aal.algorithm.AlgorithmAdapterTester;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.algbase.AlgorithmAdapterTestBase;
import org.seage.metaheuristic.genetics.BasicGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;

/**
 *
 * @author rick
 */
public class GeneticAlgorithmAdapterTest extends AlgorithmAdapterTestBase
{

    public GeneticAlgorithmAdapterTest() throws Exception
    {
        super();
    }

    @Before
    public void initAlgorithm() throws Exception
    {
        SubjectEvaluator<Subject<Integer>> se = new SubjectEvaluator<Subject<Integer>>()
        {
            @Override
            protected double[] evaluate(Subject<Integer> solution) throws Exception
            {
                double val = 0;
                for (int i = 0; i < solution.getChromosome().getGenes().length; i++)
                {
                    val += i * solution.getChromosome().getGene(i);
                }

                return new double[] { val };
            }
        };

        _algAdapter = new GeneticAlgorithmAdapter<Subject<Integer>>(
                new BasicGeneticOperator<Subject<Integer>, Integer>(), se, false, "")
        {
            private List<Subject<Integer>> _solutions0;

            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _solutions = new ArrayList<Subject<Integer>>(source.length);
                _solutions0 = new ArrayList<Subject<Integer>>(source.length);
                for (int i = 0; i < source.length; i++)
                {
                    Subject<Integer> s = new Subject<Integer>((Integer[]) source[i]);
                    _solutions.add(s);
                    _solutions0.add(s);
                }
            }

            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                Assert.assertEquals(_solutions0.size(), _solutions.size());

                for (int i = 0; i < _solutions.size(); i++)
                {
                    Assert.assertNotSame(_solutions0.get(i), _solutions.get(i));
                }
                return null;
            }
        };

        _algParams = new AlgorithmParams();
        _algParams.putValue("crossLengthPct", 40);
        _algParams.putValue("mutateLengthPct", 20);
        _algParams.putValue("eliteSubjectPct", 1);
        _algParams.putValue("iterationCount", 10);
        _algParams.putValue("mutateSubjectPct", 10);
        _algParams.putValue("numSolutions", NUM_SOLUTIONS);
        _algParams.putValue("randomSubjectPct", 1);

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
        params.putValue("crossLengthPct", 0);
        params.putValue("mutateLengthPct", 0);
        params.putValue("eliteSubjectPct", 0);
        params.putValue("iterationCount", 0);
        params.putValue("mutateSubjectPct", 0);
        params.putValue("numSolutions", 0);
        params.putValue("randomSubjectPct", 0);
        _tester.setAlgParameters(params);
        _tester.testAlgorithmWithParamsAtZero();
    }

    @Test
    @Override
    public void testAsyncRunning() throws Exception
    {
        AlgorithmParams params = new AlgorithmParams();
        params.putValue("crossLengthPct", 1);
        params.putValue("mutateLengthPct", 1);
        params.putValue("eliteSubjectPct", 1);
        params.putValue("iterationCount", 1000000);
        params.putValue("mutateSubjectPct", 1);
        params.putValue("numSolutions", 10);
        params.putValue("randomSubjectPct", 1);
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
