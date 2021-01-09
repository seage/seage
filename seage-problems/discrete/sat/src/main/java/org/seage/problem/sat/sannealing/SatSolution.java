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
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.problem.sat.sannealing;

import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Richard Malek
 */
public class SatSolution extends Solution {
  private boolean[] _literalValues;
  private int _hash;

  public SatSolution(boolean[] literalValues) {
    _literalValues = literalValues.clone();
    // _hash = SatPhenotypeEvaluator.hashCode(_literalValues);
  }

  public boolean[] getLiteralValues() {
    return _literalValues;
  }

  @Override
  public Solution clone() {
    SatSolution copy = (SatSolution) super.clone();

    copy._literalValues = new boolean[_literalValues.length];
    for (int i = 0; i < _literalValues.length; i++) {
      copy._literalValues[i] = _literalValues[i];
    }

    return copy;
  }

  @Override
  public String toString() {
    String result = super.toString();

    String str = "";
    for (int i = 0; i < _literalValues.length; i++) {
      int val = _literalValues[i] == true ? 1 : 0;
      str += val;
    }
    return result + "\t" + str;
  }

  @Override
  public int hashCode() {
    return _hash;
  }

}
