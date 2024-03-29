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

package org.seage.problem.tsp.genetics;

import java.util.ArrayList;
import java.util.Arrays;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.algorithm.genetics.GeneticAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("GeneticAlgorithm")
@Annotations.AlgorithmName("GeneticAlgorithm")
public class TspGeneticAlgorithmFactory implements IAlgorithmFactory<TspPhenotype, Subject<Integer>> {

  @Override
  public Class<?> getAlgorithmClass() {
    return GeneticAlgorithmAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<TspPhenotype, Subject<Integer>> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator) throws Exception {
    City[] cities = ((TspProblemInstance) instance).getCities();
    IAlgorithmAdapter<TspPhenotype, Subject<Integer>> algorithm = new GeneticAlgorithmAdapter<TspPhenotype, Subject<Integer>>(
        new TspGeneticOperator(), new TspEvaluator(phenotypeEvaluator), phenotypeEvaluator, false) {
      @Override
      public void solutionsFromPhenotype(TspPhenotype[] source) throws Exception {
        this.solutions = new ArrayList<Subject<Integer>>(source.length);
        for (int i = 0; i < source.length; i++)
          this.solutions.add(new Subject<Integer>(source[i].getSolution()));
      }

      @Override
      public TspPhenotype[] solutionsToPhenotype() throws Exception {
        TspPhenotype[] result = new TspPhenotype[this.solutions.size()];

        for (int i = 0; i < this.solutions.size(); i++) {
          result[i] = solutionToPhenotype(this.solutions.get(i));
        }
        return result;
      }

      public TspPhenotype solutionToPhenotype(Subject<Integer> solution) throws Exception {
        TspPhenotype result = new TspPhenotype(solution.getChromosome().getGenes());
        double[] objVals = this.phenotypeEvaluator.evaluate(result);
        result.setObjValue(objVals[0]);
        result.setScore(objVals[1]);
        return result;
      }
    };

    return algorithm;
  }
}
