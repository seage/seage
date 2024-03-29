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

/**
 *
 * @author Martin Zaloga
 */
public class SatTestSolution extends SatSolution {
  private static final long serialVersionUID = -1532470194515354499L;

  public SatTestSolution(int countLiterals) {
    super(null, null);
    initTestSol(countLiterals);
  }

  private void initTestSol(int countLiterals) {
    _litValues = new Boolean[countLiterals];
    for (int i = 0; i < countLiterals; i++) {
      _litValues[i] = true;
    }
  }
}
