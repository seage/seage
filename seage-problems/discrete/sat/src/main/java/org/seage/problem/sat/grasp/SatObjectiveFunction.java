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
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.sat.grasp;

import java.io.Serializable;
import org.seage.data.ObjectCloner;
import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.IObjectiveFunction;
import org.seage.metaheuristic.grasp.Solution;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatObjectiveFunction implements IObjectiveFunction, Serializable {

    private Formula _formula;

    public SatObjectiveFunction(Formula formula) {
        _formula = formula;
    }

    public void reset() {
    }

    //OK
    public double evaluateMove(Solution sol, IMove move) throws Exception
    {

        if(move == null)
            return evaluate((SatSolution)sol);
        else
        {
            SatSolution s = (SatSolution)ObjectCloner.deepCopy(sol);
            move.apply(s);
            return evaluate(s);
        }
    }

    private int evaluate(SatSolution sol)
    {
        return FormulaEvaluator.evaluate(_formula, sol.getLiteralValues());
    }

}