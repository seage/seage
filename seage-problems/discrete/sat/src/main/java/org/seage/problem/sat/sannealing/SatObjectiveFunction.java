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
 * Contributors: Jan Zmatlik - Initial implementation
 */
package org.seage.problem.sat.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
public class SatObjectiveFunction implements IObjectiveFunction {
  private SatPhenotypeEvaluator _evaluator;

  public SatObjectiveFunction(Formula formula) {
    _evaluator = new SatPhenotypeEvaluator(formula);
  }

  @Override
  public double getObjectiveValue(Solution solution) throws Exception {
    SatSolution sol = (SatSolution) solution;
    Boolean[] vals = new Boolean[sol.getLiteralValues().length];
    for (int i = 0; i < vals.length; i++)
      vals[i] = sol.getLiteralValues()[i];
    return 0.0;// _evaluator.evaluate(vals)[0];
  }
}
