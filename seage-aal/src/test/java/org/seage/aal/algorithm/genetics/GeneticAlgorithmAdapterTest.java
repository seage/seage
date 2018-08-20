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
import org.seage.aal.algorithm.AlgorithmAdapterTestBase;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.genetics.BasicGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;

/**
 *
 * @author Richard Malek
 */
public class GeneticAlgorithmAdapterTest extends AlgorithmAdapterTestBase<Subject<Integer>>
{

    public GeneticAlgorithmAdapterTest() throws Exception
    {
        super();
    }

    @BeforeEach
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

        _algAdapter = new GeneticAlgorithmAdapter<TestPhenotype, Subject<Integer>>(
                new BasicGeneticOperator<Subject<Integer>, Integer>(), se, null, false)
        {
            private List<Subject<Integer>> _solutions0;

            @Override
            public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception
            {
                _solutions = new ArrayList<Subject<Integer>>(source.length);
                _solutions0 = new ArrayList<Subject<Integer>>(source.length);
                for (int i = 0; i < source.length; i++)
                {
                    Subject<Integer> s = new Subject<Integer>(source[i].getSolution());
                    _solutions.add(s);
                    _solutions0.add(s);
                }
            }

            @Override
            public TestPhenotype[] solutionsToPhenotype() throws Exception
            {
                assertEquals(_solutions0.size(), _solutions.size());

                for (int i = 0; i < _solutions.size(); i++)
                {
                    assertNotSame(_solutions0.get(i), _solutions.get(i));
                }
                return null;
            }

			@Override
			public TestPhenotype solutionToPhenotype(Subject<Integer> solution) throws Exception {
				// TODO Auto-generated method stub
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

        //_tester = new AlgorithmAdapterTestImpl<>(_algAdapter, /*_solutions,*/ _algParams);
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
