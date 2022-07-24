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

import java.util.Random;

import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Martin Zaloga
 */
public class SatRandomSolution extends SatSolution {
  private static final long serialVersionUID = -3426994807179668189L;

  private int _countLiterals;

  public SatRandomSolution(Formula formula) {
    super();
    _countLiterals = formula.getLiteralCount();
    initRandSol(formula);
  }

  private void initRandSol(Formula formula) {
    Random rnd = new Random();
    _litValues = new Boolean[_countLiterals];
    for (int i = 0; i < _countLiterals; i++) {
      _litValues[i] = rnd.nextBoolean();
    }
    setObjectiveValue(FormulaEvaluator.evaluate(formula, _litValues));
  }
}
