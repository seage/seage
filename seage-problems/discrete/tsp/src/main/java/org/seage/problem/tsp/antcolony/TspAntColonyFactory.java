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

package org.seage.problem.tsp.antcolony;

import java.util.Arrays;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspProblemInstance;

/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("AntColony")
@Annotations.AlgorithmName("AntColony")
public class TspAntColonyFactory implements IAlgorithmFactory<TspPhenotype, Ant> {
  @Override
  public Class<?> getAlgorithmClass() {
    return AntColonyAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<TspPhenotype, Ant> createAlgorithm(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator) throws Exception {
    City[] cities = ((TspProblemInstance) instance).getCities();
    TspGraph graph = new TspGraph(cities);
    TspAntBrain brain = new TspAntBrain(graph);
    IAlgorithmAdapter<TspPhenotype, Ant> algorithm = 
        new AntColonyAdapter<TspPhenotype, Ant>(
        graph, phenotypeEvaluator) {
      @Override
      public void solutionsFromPhenotype(TspPhenotype[] source) throws Exception {
        _ants = new Ant[source.length];
        for (int i = 0; i < _ants.length; i++) {
          _ants[i] = new Ant(brain, graph, Arrays.asList(source[i].getSolution()));
        }
      }

      @Override
      public TspPhenotype[] solutionsToPhenotype() throws Exception {
        TspPhenotype[] result = new TspPhenotype[_ants.length];
        for (int i = 0; i < _ants.length; i++) {          
          result[i] = solutionToPhenotype(_ants[i]);
        }
        return result;
      }

      @Override
      public TspPhenotype solutionToPhenotype(Ant solution) throws Exception {
        Integer[] p = (Integer[]) solution.getNodeIDsAlongPath().stream().toArray(Integer[]::new);
        return new TspPhenotype(p);
      }
    };

    return algorithm;
  }

}