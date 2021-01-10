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
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.tsp.grasp;

import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class TspMove implements IMove {

  /**
   * _ix1, _ix2 - Indices of the cities that will be swapped
   */
  int _ix1;
  int _ix2;

  /*
   * Constructor the object, which defines the elementary step of the algorithm
   */
  public TspMove(int ix1, int ix2) {
    _ix1 = ix1;
    _ix2 = ix2;
  }

  /**
   * Function for geting one index
   * 
   * @return - First index
   */
  public int getIx1() {
    return _ix1;
  }

  /**
   * Function for geting second index
   * 
   * @return - Second index
   */
  public int getIx2() {
    return _ix2;
  }

  /**
   * Function for the applycation the actual solution as new solution
   * 
   * @param s - Actual solution
   * @return - New solution
   */
  @Override
  public Solution apply(Solution s) {
    TspSolution newSol = (TspSolution) s;
    Integer[] newTour = newSol.getTour();
    int pom = newTour[_ix1];
    newTour[_ix1] = newTour[_ix2];
    newTour[_ix2] = pom;
    newSol.setTour(newTour);
    return newSol;
  }
}
