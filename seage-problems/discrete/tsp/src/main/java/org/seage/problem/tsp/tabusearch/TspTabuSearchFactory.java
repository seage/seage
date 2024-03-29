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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.data.DataNode;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class TspTabuSearchFactory implements IAlgorithmFactory<TspPhenotype, TspSolution> {
  @Override
  public Class<?> getAlgorithmClass() {
    return TabuSearchAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<TspPhenotype, TspSolution> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator) throws Exception {
    final City[] cities = ((TspProblemInstance) instance).getCities();

    IAlgorithmAdapter<TspPhenotype, TspSolution> algorithm = new TabuSearchAdapter<TspPhenotype, TspSolution>(
        new TspMoveManager(), new TspObjectiveFunction(cities), phenotypeEvaluator) {

      @Override
      public void solutionsFromPhenotype(TspPhenotype[] source) throws Exception {
        this.solutions = new TspSolution[source.length];
        for (int i = 0; i < source.length; i++) {
          TspSolution s = new TspSolution();
          Integer[] tour = new Integer[source[i].getSolution().length];
          for (int j = 0; j < tour.length; j++)
            tour[j] = (Integer) source[i].getSolution()[j];
          s.setTour(tour);
          this.solutions[i] = s;
        }
      }

      @Override
      public TspPhenotype[] solutionsToPhenotype() throws Exception {
        TspPhenotype[] result = new TspPhenotype[this.solutions.length];

        for (int i = 0; i < this.solutions.length; i++) {
          result[i] = solutionToPhenotype((TspSolution) this.solutions[i]);
        }
        return result;
      }

      @Override
      public TspPhenotype solutionToPhenotype(TspSolution solution) throws Exception {
        TspPhenotype result = new TspPhenotype(solution.getTour());
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

  public DataNode getAlgorithmParameters(DataNode params) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
