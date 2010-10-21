/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.qap.sannealing;

import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.sannealing.SimulatedAnnealingAdapter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.qap.QapProblemProvider;

/**
 *
 * @author Karel Durkota
 */
public class QapSimulatedAnnealingFactory implements IAlgorithmFactory
{
    private QapSolution _qapSolution;

    public QapSimulatedAnnealingFactory()
    {
    }


    public QapSimulatedAnnealingFactory(DataNode params, Double[][][] facilityLocation) throws Exception
    {
        String solutionType = params.getValueStr("initSolutionType");
        if( solutionType.toLowerCase().equals("greedy") )
            _qapSolution = new QapGreedySolution( facilityLocation );
        else if( solutionType.toLowerCase().equals("random") )
            _qapSolution = new QapRandomSolution( facilityLocation );
        else if( solutionType.toLowerCase().equals("sorted") )
            _qapSolution = new QapSortedSolution( facilityLocation );
    }

    public IAlgorithmAdapter createAlgorithm(DataNode algorithmParams) throws Exception
    {
        IAlgorithmAdapter algorithm;

        algorithm = new SimulatedAnnealingAdapter((Solution) _qapSolution,
                new QapObjectiveFunction(),
                new QapMoveManager(), false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception 
            {
                QapSolution initialSolution = new QapGreedySolution(QapProblemProvider.getFacilityLocation());
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
