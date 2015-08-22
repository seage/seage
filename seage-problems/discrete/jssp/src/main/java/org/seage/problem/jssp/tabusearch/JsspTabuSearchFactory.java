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
package org.seage.problem.jssp.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.jssp.JobsDefinition;
import org.seage.problem.jssp.JsspPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class JsspTabuSearchFactory implements IAlgorithmFactory
{

    @Override
    public Class<TabuSearchAdapter> getAlgorithmClass()
    {
        return TabuSearchAdapter.class;
    }

    @Override
    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance) throws Exception
    {
        JsspPhenotypeEvaluator evaluator = new JsspPhenotypeEvaluator((JobsDefinition) instance);
       
        IAlgorithmAdapter algorithm = new TabuSearchAdapter(new JsspMoveManager(evaluator),
                new JsspObjectiveFunction(evaluator), "")
        {

            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _solutions = new Solution[source.length];
                for (int i = 0; i < source.length; i++)
                {
                    Integer[] sol = new Integer[source[i].length];
                    for (int j = 0; j < sol.length; j++)
                        sol[j] = (Integer) source[i][j];

                    _solutions[i] = new JsspSolution(sol);
                }
            }

            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                Object[][] result = new Object[_solutions.length][];

                for (int i = 0; i < _solutions.length; i++)
                {
                    JsspSolution s = (JsspSolution) _solutions[i];
                    result[i] = s.getJobArray();                        
                }
                return result;
            }
        };

        return algorithm;
    }
}
