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
package org.seage.problem.jssp.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.jssp.JsspPhenotypeEvaluator;

/**
 * Summary description for JsspObjectiveFunction.
 */
public class JsspObjectiveFunction implements ObjectiveFunction
{
    private JsspPhenotypeEvaluator _evaluator;

    public JsspObjectiveFunction(JsspPhenotypeEvaluator evaluator)
    {
        _evaluator = evaluator;
    }

    @Override
    public double[] evaluate(Solution soln, Move move) throws Exception
    {
        if (move != null)
            move.operateOn(soln);
        Integer[] jobArray = ((JsspSolution) soln).getJobArray();
        double[] values = _evaluator.evaluate(jobArray);
        if (move != null)
            move.operateOn(soln);

        return values;
    }
}
