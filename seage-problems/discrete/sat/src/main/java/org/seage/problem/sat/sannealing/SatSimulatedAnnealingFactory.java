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
 * Contributors: Karel Durkota - Initial implementation Richard Malek - Added
 * algorithm annotations
 */

package org.seage.problem.sat.sannealing;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.sannealing.SimulatedAnnealingAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.SatPhenotype;

/**
 * SatSimulatedAnnealingFactory class.
 * @author Richard Malek
 */
@Annotations.AlgorithmId("SimulatedAnnealing")
@Annotations.AlgorithmName("Simulated Annealing")
public class SatSimulatedAnnealingFactory implements IAlgorithmFactory<SatPhenotype, SatSolution> {

  @Override
  public Class<?> getAlgorithmClass() {
    return SimulatedAnnealingAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<SatPhenotype, SatSolution> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<SatPhenotype> phenotypeEvaluator) throws Exception {
    Formula formula = (Formula) instance;
    IAlgorithmAdapter<SatPhenotype, SatSolution> algorithm = 
        new SimulatedAnnealingAdapter<SatPhenotype, SatSolution>(
            new SatObjectiveFunction(phenotypeEvaluator), new SatMoveManager(), phenotypeEvaluator, false) {

      @Override
      public void solutionsFromPhenotype(SatPhenotype[] source) throws Exception {
        this.solutions = new SatSolution[source.length];
        for (int i = 0; i < source.length; i++) {
          this.solutions[i] = new SatSolution(source[i].getSolution());
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
      public SatPhenotype solutionToPhenotype(SatSolution solution) throws Exception {
        SatSolution s = (SatSolution) solution;
        SatPhenotype result = new SatPhenotype(s.getLiteralValues());
        double[] objVals = this._phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }
    };
    return algorithm;
  }

}
