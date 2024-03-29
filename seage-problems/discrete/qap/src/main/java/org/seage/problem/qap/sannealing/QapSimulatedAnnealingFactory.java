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
 *     Karel Durkota
 *     - Initial implementation
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.qap.sannealing;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.sannealing.SimulatedAnnealingAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemInstance;

/**
 *
 * @author Karel Durkota
 */
@Annotations.AlgorithmId("SimulatedAnnealing")
@Annotations.AlgorithmName("Simulated Annealing")
@Annotations.NotReady
public class QapSimulatedAnnealingFactory implements IAlgorithmFactory<QapPhenotype, QapSolution>
{

    //    public QapSimulatedAnnealingFactory(DataNode params, Double[][][] facilityLocation) throws Exception
    //    {
    //        String solutionType = params.getValueStr("initSolutionType");
    //        if( solutionType.toLowerCase().equals("greedy") )
    //            _qapSolution = new QapGreedySolution( facilityLocation );
    //        else if( solutionType.toLowerCase().equals("random") )
    //            _qapSolution = new QapRandomSolution( facilityLocation );
    //        else if( solutionType.toLowerCase().equals("sorted") )
    //            _qapSolution = new QapSortedSolution( facilityLocation );
    //    }

    @Override
    public Class<?> getAlgorithmClass()
    {
        return SimulatedAnnealingAdapter.class;
    }

    // @Override
    // public IAlgorithmAdapter<QapSolution> createAlgorithm(ProblemInstance instance) throws Exception
    // {
    //     final Double[][][] facilityLocation = ((QapProblemInstance) instance).getFacilityLocation();

    //     IAlgorithmAdapter<QapSolution> algorithm = new SimulatedAnnealingAdapter<>(
    //             new QapObjectiveFunction(),
    //             new QapMoveManager(), false, "")
    //     {
    //         @Override
    //         public void solutionsFromPhenotype(Object[][] source) throws Exception
    //         {
    //             this.solutions = new Solution[source.length];
    //             for (int j = 0; j < source.length; j++)
    //             {
    //                 QapSolution solution = new QapGreedySolution(facilityLocation);
    //                 Integer[] assign = solution.getAssign();

    //                 for (int i = 0; i < assign.length; i++)
    //                     assign[i] = (Integer) source[0][i];

    //                 this.solutions[j] = solution;
    //             }
    //         }

    //         @Override
    //         public Object[][] solutionsToPhenotype() throws Exception
    //         {
    //             Integer[] assign = ((QapSolution) _simulatedAnnealing.getBestSolution()).getAssign();
    //             Object[][] source = new Object[1][assign.length];

    //             for (int j = 0; j < source.length; j++)
    //             {
    //                 source[j] = new Integer[assign.length];
    //                 for (int i = 0; i < assign.length; i++)
    //                     source[j][i] = assign[i];
    //             }
    //             return source;
    //         }

	// 		@Override
	// 		public Object[] solutionToPhenotype(QapSolution solution) throws Exception {
	// 			// TODO Auto-generated method stub
	// 			return null;
	// 		}

    //     };

    //     return algorithm;
    // }

    @Override
    public IAlgorithmAdapter<QapPhenotype, QapSolution> createAlgorithm(ProblemInstance instance,
            IPhenotypeEvaluator<QapPhenotype> phenotypeEvaluator) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

}
