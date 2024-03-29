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
 * Contributors: Martin Zaloga - Initial implementation
 */
package org.seage.problem.sat.grasp;

import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class SatMove implements IMove {

  private int _literalIx;

  public SatMove(int literalIx) {
    _literalIx = literalIx;
  }

  public int getLiteralIx() {
    return _literalIx;
  }

  @Override
  public Solution apply(Solution s) {
    // TODO: A - tady se musi 's' klonovat - (hluboka kopie)
    SatSolution newSol = (SatSolution) s;
    Boolean[] litValues = newSol._litValues.clone();
    if (litValues[_literalIx]) {
      litValues[_literalIx] = false;
    } else {
      litValues[_literalIx] = true;
    }
    newSol = new SatSolution(null, null);
    newSol.setLiteralValues(litValues);
    return newSol;
  }
}
