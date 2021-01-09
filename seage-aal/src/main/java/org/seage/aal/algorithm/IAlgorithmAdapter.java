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
package org.seage.aal.algorithm;

import org.seage.aal.reporter.AlgorithmReport;

/**
 * IAlgorithmAdapter interface.
 *
 * @author Richard Malek
 */
public interface IAlgorithmAdapter<P extends Phenotype<?>, S> {
  // // Returns meta-data on the algorithm
  // // |_ id
  // // |_ name
  // // |_ class
  // // |_ parameters
  // DataNode getAlgorithmInfo() throws Exception;

  // Runs the algorithm.
  void startSearching(final AlgorithmParams params) throws Exception;

  // Runs the algorithm.
  void startSearching(final AlgorithmParams params, boolean async) throws Exception;

  // Stops the algorithm.
  void stopSearching() throws Exception;

  // Checks if the algorithm is running
  boolean isRunning();

  // Returns the runtime report collected during the algorithm run.
  AlgorithmReport getReport() throws Exception;

  // Converts solution from outer representation to the inner one.
  void solutionsFromPhenotype(P[] source) throws Exception;

  // Converts solution from inner representation to the outer one.
  // Solutions are ordered in descendent (best first) order according to the
  // quality.
  P[] solutionsToPhenotype() throws Exception;

  P solutionToPhenotype(S solution) throws Exception;
}
