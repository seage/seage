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
 * Contributors: Jan Zmatlik - Initial implementation Richard Malek - Added algorithm annotations
 * David Omrai - Editation and debugging
 */
package org.seage.problem.jsp.sannealing;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.sannealing.SimulatedAnnealingAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

/**
 *
 * @author Jan Zmatlik
 */
@Annotations.AlgorithmId("SimulatedAnnealing")
@Annotations.AlgorithmName("Simulated Annealing")
public class JspSimulatedAnnealingFactory
    implements IAlgorithmFactory<JspPhenotype, JspSimulatedAnnealingSolution> {
  @Override
  public Class<?> getAlgorithmClass() {
    return SimulatedAnnealingAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<JspPhenotype, JspSimulatedAnnealingSolution> createAlgorithm(
      ProblemInstance instance, IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator)
      throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    JspPhenotypeEvaluator evaluator =
        new JspPhenotypeEvaluator(problemProvider.getProblemInfo(), (JspJobsDefinition) instance);
    JspObjectiveFunction objFun = new JspObjectiveFunction(evaluator);
    return new SimulatedAnnealingAdapter<JspPhenotype, JspSimulatedAnnealingSolution>(objFun,
        new JspMoveManager((JspJobsDefinition) instance, objFun), phenotypeEvaluator, false) {
      @Override
      public void solutionsFromPhenotype(JspPhenotype[] source) throws Exception {
        this.solutions = new JspSimulatedAnnealingSolution[source.length];
        for (int i = 0; i < this.solutions.length; i++)
          this.solutions[i] = new JspSimulatedAnnealingSolution(source[i].getSolution());
      }

      @Override
      public JspPhenotype[] solutionsToPhenotype() throws Exception {
        JspPhenotype[] result = new JspPhenotype[this.solutions.length];
        for (int i = 0; i < this.solutions.length; i++) {
          result[i] = solutionToPhenotype(this.solutions[i]);
        }
        return result;
      }

      @Override
      public JspPhenotype solutionToPhenotype(JspSimulatedAnnealingSolution solution)
          throws Exception {
        JspPhenotype result = new JspPhenotype(solution.getJobArray());
        double[] objVals = this.phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }
    };
  }
}
