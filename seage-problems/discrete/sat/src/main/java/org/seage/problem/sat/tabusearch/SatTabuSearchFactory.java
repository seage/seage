/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.problem.sat.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.data.DataNode;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class SatTabuSearchFactory implements IAlgorithmFactory
{

    public Class<TabuSearchAdapter> getAlgorithmClass()
    {
        return TabuSearchAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance) throws Exception
    {
        IAlgorithmAdapter algorithm;

        Formula formula = (Formula) instance;

        algorithm = new TabuSearchAdapter(new SatMoveManager(),
                new SatObjectiveFunction(new SatPhenotypeEvaluator(formula)), "")
        {

            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _solutions = new Solution[source.length];
                for (int i = 0; i < source.length; i++)
                {
                    boolean[] sol = new boolean[source[i].length];
                    for (int j = 0; j < sol.length; j++)
                        sol[j] = (Boolean) source[i][j];

                    _solutions[i] = new SatSolution(sol);
                }
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                Object[][] result = new Object[_solutions.length][];

                for (int i = 0; i < _solutions.length; i++)
                {
                    SatSolution s = (SatSolution) _solutions[i];
                    result[i] = new Boolean[s.getLiteralValues().length];
                    for (int j = 0; j < s.getLiteralValues().length; j++)
                    {
                        result[i][j] = s.getLiteralValues()[j];
                    }
                }
                return result;
            }
        };

        return algorithm;
    }

    public DataNode getAlgorithmParameters(DataNode params) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
