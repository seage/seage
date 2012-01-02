/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.data.ProblemConfig;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.data.DataNode;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class TspTabuSearchFactory implements IAlgorithmFactory
{

    public Class getAlgorithmClass() {
        return TabuSearchAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, ProblemConfig  config) throws Exception
    {
        IAlgorithmAdapter algorithm;

        final City[] cities = ((TspProblemInstance)instance).getCities();

        algorithm = new TabuSearchAdapter(new TspMoveManager(), new TspObjectiveFunction(cities), new TspLongTermMemory(), "" ) {

            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _solutions = new Solution[source.length];
                for(int i=0;i<source.length;i++)
                {
                    TspSolution s = new TspSolution();
                    int[] tour = new int[source[i].length];
                    for(int j=0;j<tour.length;j++)
                        tour[j] = (Integer)source[i][j];
                    s.setTour(tour);
                    _solutions[i] = s;
                }
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                Object[][] result = new Object[_solutions.length][];

                for(int i=0;i<_solutions.length;i++)
                {
                    TspSolution s = (TspSolution)_solutions[i];
                    result[i] = new Integer[s.getTour().length];
                    for(int j=0;j<s.getTour().length;j++)
                    {
                        result[i][j] = s.getTour()[j];
                    }
                }
                return result;
            }
        };

        //Object[][] solutions = _provider.generateInitialSolutions( _algParams.getValueInt("numSolution"));
        //algorithm.solutionsFromPhenotype(solutions);

        return algorithm;
    }

    public DataNode getAlgorithmParameters(DataNode params) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
