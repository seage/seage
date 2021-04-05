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
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.sat.genetics;

import java.util.ArrayList;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("GeneticAlgorithm")
@Annotations.AlgorithmName("GeneticAlgorithm")
public class SatGeneticAlgorithmFactory
    implements IAlgorithmFactory<SatPhenotype, Subject<Boolean>> {

  @Override
  public Class<?> getAlgorithmClass() {
    return GeneticAlgorithmAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<SatPhenotype, Subject<Boolean>> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<SatPhenotype> phenotypeEvaluator) throws Exception {
    // Formula formula = (Formula) instance;
    IAlgorithmAdapter<SatPhenotype, Subject<Boolean>> algorithm =
        new GeneticAlgorithmAdapter<SatPhenotype, Subject<Boolean>>(new SatGeneticOperator(),
            new SatEvaluator(phenotypeEvaluator), phenotypeEvaluator, false) {
          @Override
          public void solutionsFromPhenotype(SatPhenotype[] source) throws Exception {
            this.solutions = new ArrayList<Subject<Boolean>>(source.length);
            for (int i = 0; i < source.length; i++) {
              this.solutions.add(new Subject<Boolean>(source[i].getSolution()));
            }
          }

          @Override
          public SatPhenotype[] solutionsToPhenotype() throws Exception {
            SatPhenotype[] result = new SatPhenotype[this.solutions.size()];

            for (int i = 0; i < this.solutions.size(); i++) {
              result[i] = new SatPhenotype(this.solutions.get(i).getChromosome().getGenes());
            }
            return result;
          }

          @Override
          public SatPhenotype solutionToPhenotype(Subject<Boolean> s) throws Exception {
            Boolean[] array = new Boolean[s.getChromosome().getLength()];
            for (int j = 0; j < array.length; j++) {
              array[j] = s.getChromosome().getGene(j);
            }
            SatPhenotype result = new SatPhenotype(array);
            double[] objVals = this._phenotypeEvaluator.evaluate(result);
            result.setObjValue(objVals[0]);
            result.setObjValue(objVals[1]);
            return result;
          }
        };

    return algorithm;
  }

}
