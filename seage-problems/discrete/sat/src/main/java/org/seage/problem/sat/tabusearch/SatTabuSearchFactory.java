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
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.problem.sat.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.data.DataNode;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class SatTabuSearchFactory implements IAlgorithmFactory<SatPhenotype, SatSolution> {

  @Override
  public Class<?> getAlgorithmClass() {
    return TabuSearchAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<SatPhenotype, SatSolution> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<SatPhenotype> phenotypeEvaluator) throws Exception {
    Formula formula = (Formula) instance;

    IAlgorithmAdapter<SatPhenotype, SatSolution> algorithm = new TabuSearchAdapter<SatPhenotype, SatSolution>(
        new SatMoveManager(), new SatObjectiveFunction(phenotypeEvaluator), phenotypeEvaluator) {

      @Override
      public void solutionsFromPhenotype(SatPhenotype[] source) throws Exception {
        this.solutions = new SatSolution[source.length];
        for (int i = 0; i < source.length; i++) {
          boolean[] sol = new boolean[source[i].getSolution().length];
          for (int j = 0; j < sol.length; j++)
            sol[j] = (Boolean) source[i].getSolution()[j];

          this.solutions[i] = new SatSolution(sol);
        }
      }

      @Override
      public SatPhenotype[] solutionsToPhenotype() throws Exception {
        SatPhenotype[] result = new SatPhenotype[this.solutions.length];

        for (int i = 0; i < this.solutions.length; i++) {
          result[i] = solutionToPhenotype(this.solutions[i]);
        }
        return result;
      }

      @Override
      public SatPhenotype solutionToPhenotype(SatSolution s) throws Exception {
        Boolean[] array = new Boolean[s.getLiteralValues().length];
        for (int j = 0; j < s.getLiteralValues().length; j++) {
          array[j] = s.getLiteralValues()[j];
        }
        SatPhenotype result = new SatPhenotype(array);
        double[] objVals = this.phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }
    };

    return algorithm;
  }

  public DataNode getAlgorithmParameters(DataNode params) throws Exception {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
