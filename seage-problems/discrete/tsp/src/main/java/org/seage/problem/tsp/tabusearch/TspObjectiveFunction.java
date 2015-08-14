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
package org.seage.problem.tsp.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspMoveBasedEvaluator;

/**
 *
 * @author Richard Malek
 */
public class TspObjectiveFunction implements ObjectiveFunction
{
    protected TspMoveBasedEvaluator _evaluator;

    public TspObjectiveFunction(City[] cities)
    {
        _evaluator = new TspMoveBasedEvaluator(cities.clone());
    } // end constructor

    @Override
    public double[] evaluate(Solution solution, Move move) throws Exception
    {
        int[] move2 = null;
        double[] objVal = solution.getObjectiveValue();
        if (move != null)
        {
            TspMove tspMove = (TspMove) move;
            move2 = new int[] { tspMove.ix1, tspMove.ix2 };
        }
        return _evaluator.evaluate(((TspSolution) solution).getTour(), move2, objVal == null ? 0 : objVal[0]);
    }

} // end class MyObjectiveFunction
