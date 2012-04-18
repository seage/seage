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
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.aal.algorithm.sannealing.SimulatedAnnealingAdapter;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.qap.QapProblemInstance;

/**
 *
 * @author Karel Durkota
 */
@Annotations.AlgorithmId("SimulatedAnnealing")
@Annotations.AlgorithmName("Simulated Annealing")
public class QapSimulatedAnnealingFactory implements IAlgorithmFactory
{
    private QapSolution _qapSolution;

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

    public Class getAlgorithmClass() {
        return SimulatedAnnealingAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, ProblemConfig config) throws Exception
    {
        final Double[][][] facilityLocation = ((QapProblemInstance)instance).getFacilityLocation();
        
        IAlgorithmAdapter algorithm;

        algorithm = new SimulatedAnnealingAdapter((Solution) _qapSolution,
                new QapObjectiveFunction(),
                new QapMoveManager(), false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception 
            {
                QapSolution initialSolution = new QapGreedySolution(facilityLocation);
                Integer[] assign = initialSolution.getAssign();

                for(int i = 0; i < assign.length; i++)
                    assign[i] = (Integer)source[0][i];
                
                _initialSolution = initialSolution;
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                Integer[] assign = ((QapSolution) _simulatedAnnealing.getBestSolution()).getAssign();
                Object[][] source = new Object[1][ assign.length ];

                source[0] = new Integer[ assign.length ];
                for(int i = 0; i < assign.length; i++)
                    source[0][i] = assign[i];

                return source;
            }

        };

        return algorithm;
    }

}
