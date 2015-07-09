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
import org.seage.aal.algorithm.sannealing.SimulatedAnnealingAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Jan Zmatlik
 */
@Annotations.AlgorithmId("SimulatedAnnealing")
@Annotations.AlgorithmName("Simulated Annealing")
public class TspSimulatedAnnealingFactory implements IAlgorithmFactory
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


    public Class<SimulatedAnnealingAdapter> getAlgorithmClass() {
        return SimulatedAnnealingAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance) throws Exception
    {
        IAlgorithmAdapter algorithm;
        final City[] cities = ((TspProblemInstance)instance).getCities();
        
        algorithm = new SimulatedAnnealingAdapter(
                new TspObjectiveFunction(cities),
                new TspMoveManager(), false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception 
            {
            	_solutions = new Solution[source.length];
            	for(int j=0;j<source.length;j++)
            	{
	                TspSolution solution = new TspGreedySolution(cities);
	                Integer[] tour = solution.getTour();
	
	                for(int i = 0; i < tour.length; i++)
	                    tour[i] = (Integer)source[j][i];
                
	                _solutions[j] = solution;
            	}
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                Object[][] result = new Object[_solutions.length][];

                for(int i=0;i<_solutions.length;i++)
            	{
                	TspSolution solution = (TspSolution)_solutions[i]; 
                	result[i] = solution.getTour().clone();                	
            	}
                return result;
            }

        };

        return algorithm;
    }

}
