/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation David Omrai - Following implementation
 */
package org.seage.problem.jsp.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class JspTabuSearchFactory implements IAlgorithmFactory<JspPhenotype, JspTabuSearchSolution> {

  @Override
  public Class<?> getAlgorithmClass() {
    return TabuSearchAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<JspPhenotype, JspTabuSearchSolution> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator) throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotypeEvaluator evaluator =
        new JspPhenotypeEvaluator(problemProvider.getProblemInfo(), (JobsDefinition) instance);
    return new TabuSearchAdapter<JspPhenotype, JspTabuSearchSolution>(new JspMoveManager((JobsDefinition) instance),
        new JspObjectiveFunction(evaluator), phenotypeEvaluator) {

      @Override
      public void solutionsFromPhenotype(JspPhenotype[] source) throws Exception {
        this.solutions = new JspTabuSearchSolution[source.length];
        for (int i = 0; i < source.length; i++)
          this.solutions[i] = new JspTabuSearchSolution(source[i].getSolution());
      }

      @Override
      public JspPhenotype[] solutionsToPhenotype() throws Exception {
        JspPhenotype[] result = new JspPhenotype[this.solutions.length];
        for (int i = 0; i < this.solutions.length; i++) {
          JspTabuSearchSolution s = (JspTabuSearchSolution) this.solutions[i];
          result[i] = solutionToPhenotype(s);
        }
        return result;
      }

      @Override
      public JspPhenotype solutionToPhenotype(JspTabuSearchSolution solution) throws Exception {
        JspPhenotype result = new JspPhenotype(solution.getJobArray());
        double[] objVals = this.phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }
    };
  }
}
