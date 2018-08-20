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
 *     Jan Zmatlik
 *     - Initial implementation
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.tsp.sannealing;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.sannealing.SimulatedAnnealingAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Jan Zmatlik
 */
@Annotations.AlgorithmId("SimulatedAnnealing")
@Annotations.AlgorithmName("Simulated Annealing")
public class TspSimulatedAnnealingFactory implements IAlgorithmFactory<TspPhenotype, TspSolution>
{
    //	private TspSolution _tspSolution;
    //    private TspProblemProvider _provider;

    public TspSimulatedAnnealingFactory()
    {
    }

    //    public TspSimulatedAnnealingFactory(DataNode params, City[] cities) throws Exception
    //    {
    //        String solutionType = params.getValueStr("initSolutionType");
    //        if( solutionType.toLowerCase().equals("greedy") )
    //            _tspSolution = new TspGreedySolution( cities );
    //        else if( solutionType.toLowerCase().equals("random") )
    //            _tspSolution = new TspRandomSolution( cities );
    //        else if( solutionType.toLowerCase().equals("sorted") )
    //            _tspSolution = new TspSortedSolution( cities );
    //    }

    @Override
    public Class<?> getAlgorithmClass()
    {
        return SimulatedAnnealingAdapter.class;
    }

    @Override
    public IAlgorithmAdapter<TspPhenotype, TspSolution> createAlgorithm(ProblemInstance instance, IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator) throws Exception
    {
        final City[] cities = ((TspProblemInstance) instance).getCities();

        IAlgorithmAdapter<TspPhenotype, TspSolution> algorithm = new SimulatedAnnealingAdapter<TspPhenotype, TspSolution>(
                new TspObjectiveFunction(cities),
                new TspMoveManager(), phenotypeEvaluator, false)
        {
            @Override
            public void solutionsFromPhenotype(TspPhenotype[] source) throws Exception
            {
                _solutions = new Solution[source.length];
                for (int j = 0; j < source.length; j++)
                {
                    TspSolution solution = new TspGreedySolution(cities);
                    Integer[] tour = solution.getTour();

                    for (int i = 0; i < tour.length; i++)
                        tour[i] = (Integer) source[j].getSolution()[i];

                    _solutions[j] = solution;
                }
            }

            @Override
            public TspPhenotype[] solutionsToPhenotype() throws Exception
            {
            	TspPhenotype[] result = new TspPhenotype[_solutions.length];

                for (int i = 0; i < _solutions.length; i++)
                {
                    TspSolution solution = (TspSolution) _solutions[i];
                    result[i] = new TspPhenotype(solution.getTour());
                }
                return result;
            }

			@Override
			public TspPhenotype solutionToPhenotype(TspSolution solution) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}

        };

        return algorithm;
    }

}