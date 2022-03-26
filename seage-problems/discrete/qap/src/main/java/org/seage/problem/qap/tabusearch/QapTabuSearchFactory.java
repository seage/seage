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
 * Contributors: Karel Durkota - Initial implementation Richard Malek - Added algorithm annotations
 */
package org.seage.problem.qap.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.qap.QapPhenotype;
import org.seage.problem.qap.QapProblemInstance;

/**
 *
 * @author Karel Durkota
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
@Annotations.NotReady
public class QapTabuSearchFactory implements IAlgorithmFactory<QapPhenotype, QapSolution> {
  @Override
  public Class<?> getAlgorithmClass() {
    return TabuSearchAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<QapPhenotype, QapSolution> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<QapPhenotype> phenotypeEvaluator) throws Exception {
    final Double[][][] facilityLocation = ((QapProblemInstance) instance).getFacilityLocation();

    IAlgorithmAdapter<QapPhenotype, QapSolution> algorithm = new TabuSearchAdapter<QapPhenotype, QapSolution>(
        new QapMoveManager(), new QapObjectiveFunction(facilityLocation), phenotypeEvaluator) {
      @Override
      public void solutionsFromPhenotype(QapPhenotype[] source) throws Exception {
        this.solutions = new QapSolution[source.length];
        for (int i = 0; i < source.length; i++) {
          QapSolution s = new QapSolution(source[i].getSolution());
          this.solutions[i] = s;
        }
      }

      @Override
      public QapPhenotype[] solutionsToPhenotype() throws Exception {
        QapPhenotype[] result = new QapPhenotype[this.solutions.length];
        for (int i = 0; i < this.solutions.length; i++) {
          result[i] = solutionToPhenotype(this.solutions[i]);
        }
        return result;
      }

      @Override
      public QapPhenotype solutionToPhenotype(QapSolution solution) throws Exception {
        QapPhenotype result = new QapPhenotype(solution.getAssign());
        double[] objVals = this.phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }
    };

    // Object[][] solutions = _provider.generateInitialSolutions(
    // _algParams.getValueInt("numSolution"));
    // algorithm.solutionsFromPhenotype(solutions);

    return algorithm;
  }

}
