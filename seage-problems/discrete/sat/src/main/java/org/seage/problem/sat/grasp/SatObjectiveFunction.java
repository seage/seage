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

import org.seage.data.ObjectCloner;
import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.IObjectiveFunction;
import org.seage.metaheuristic.grasp.Solution;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Martin Zaloga
 */
public class SatObjectiveFunction implements IObjectiveFunction {

  private Formula _formula;

  public SatObjectiveFunction(Formula formula) {
    _formula = formula;
  }

  @Override
  public void reset() {
  }

  // OK
  @Override
  public double evaluateMove(Solution sol, IMove move) throws Exception {

    if (move == null)
      return evaluate((SatSolution) sol);
    else {
      // TODO: Fix this
      // SatSolution s = (SatSolution) ObjectCloner.deepCopy(sol);
      // move.apply(s);
      // return evaluate(s);
      return 0;
    }
  }

  private double evaluate(SatSolution sol) {
    return FormulaEvaluator.evaluate(_formula, sol.getLiteralValues());
  }

}
