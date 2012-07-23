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
package org.seage.problem.sat.grasp;

import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Martin Zaloga
 */
public class SatRandomSolution extends SatSolution {
    private int _countLiterals;

    public SatRandomSolution(Formula formula) {
        super();
        _countLiterals = formula.getLiteralCount();
        initRandSol(formula);
    }

    private void initRandSol(Formula formula) {
        _litValues = new boolean[_countLiterals];
        for (int i = 0; i < _countLiterals; i++) {
            _litValues[i] = _rnd.nextBoolean();
        }
        setObjectiveValue(FormulaEvaluator.evaluate(formula, _litValues));
    }
}
