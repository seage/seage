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
package org.seage.problem.jsp.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class JspTabuSearchFactory implements IAlgorithmFactory<JspPhenotype, JspSolution>
{

    @Override
    public Class<?> getAlgorithmClass()
    {
        return TabuSearchAdapter.class;
    }

    // @Override
    // public IAlgorithmAdapter<JspPhenotype, JspSolution> createAlgorithm(ProblemInstance instance,
    //         IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator) throws Exception {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

    @Override
    public IAlgorithmAdapter<JspPhenotype, JspSolution> createAlgorithm(
        ProblemInstance instance, 
        IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator) 
        throws Exception
    {
        JspPhenotypeEvaluator evaluator = new JspPhenotypeEvaluator((JobsDefinition) instance);
       
        return new TabuSearchAdapter<JspPhenotype, JspSolution>(new JspMoveManager(evaluator),
                new JspObjectiveFunction(evaluator), "")
        {

            @Override
            public void solutionsFromPhenotype(JspPhenotype[] source) throws Exception
            {
                // this.solutions = new Solution[source.length];
                // for (int i = 0; i < source.length; i++)
                // {
                //     Integer[] sol = new Integer[source[i].length];
                //     for (int j = 0; j < sol.length; j++)
                //         sol[j] = (Integer) source[i][j];

                //     this.solutions[i] = new JspSolution(sol);
                // }
                this.solutions = new ArrayList<JspSolution>(source.length);
                for (int i = 0; i < source.length; i++)
                    this.solutions.add(new JspSolution(source[i].getSolution()));
            }

            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                Object[][] result = new Object[this.solutions.length][];

                for (int i = 0; i < this.solutions.length; i++)
                {
                    JspSolution s = (JspSolution) this.solutions[i];
                    result[i] = s.getJobArray();                        
                }
                return result;
            }

			@Override
			public Object[] solutionToPhenotype(JspSolution solution) throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
        };
    }
}
