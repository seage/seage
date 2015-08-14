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

package org.seage.problem.tsp.genetics;

import java.util.ArrayList;
import java.util.Arrays;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("GeneticAlgorithm")
@Annotations.AlgorithmName("GeneticAlgorithm")
public class TspGeneticAlgorithmFactory implements IAlgorithmFactory
{

    public Class<?> getAlgorithmClass()
    {
        return GeneticAlgorithmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance) throws Exception
    {
        IAlgorithmAdapter algorithm;
        City[] cities = ((TspProblemInstance) instance).getCities();
        algorithm = new GeneticAlgorithmAdapter<Subject<Integer>>(new TspGeneticOperator(), new TspEvaluator(cities),
                false, "")
        {
            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _solutions = new ArrayList<Subject<Integer>>(source.length);
                for (int i = 0; i < source.length; i++)
                    _solutions.add(new Subject<Integer>((Integer[]) source[i]));
            }

            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                Object[][] result = new Object[_solutions.size()][];

                for (int i = 0; i < _solutions.size(); i++)
                {
                    int numGenes = _solutions.get(i).getChromosome().getLength();
                    result[i] = Arrays.copyOf(_solutions.get(i).getChromosome().getGenes(), numGenes);
                }
                return result;
            }
        };

        return algorithm;
    }

}
