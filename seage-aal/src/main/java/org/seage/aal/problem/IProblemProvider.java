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
package org.seage.aal.problem;

import java.util.Map;

import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;

/**
 * Problem provider interface
 * 
 * @author Richard Malek
 * 
 */
public interface IProblemProvider<P extends Phenotype<?>> {
  // Returns meta-data on the problem to be solved
  // @see ProblemInfo
  ProblemInfo getProblemInfo() throws Exception;

  // Returns the algorithm factory by id
  IAlgorithmFactory<P, ?> getAlgorithmFactory(String algorithmID) throws Exception;

  // Returns all algorithm factories
  Map<String, IAlgorithmFactory<P, ?>> getAlgorithmFactories();

  // Initializes (reads) a problem instance.
  ProblemInstance initProblemInstance(ProblemInstanceInfo problemInstanceInfo) throws Exception;

  // Initializes a problem metadata generator
  ProblemMetadataGenerator<P> initProblemMetadataGenerator();

  // Initializes an evaluator of solutions in phenotype representation
  // (i.e. in general representation of a problem solution).
  IPhenotypeEvaluator<P> initPhenotypeEvaluator(ProblemInstance problemInstance) throws Exception;

  // Generates the very first solution(s).
  // Solutions can be random, hungry, or other.
  P[] generateInitialSolutions(ProblemInstance problemInstance, int numSolutions, long randomSeed) throws Exception;

  // Visualizes solution, usually produces a picture.
  public void visualizeSolution(Object[] solution, ProblemInstanceInfo problemInstanceInfo) throws Exception;
}
