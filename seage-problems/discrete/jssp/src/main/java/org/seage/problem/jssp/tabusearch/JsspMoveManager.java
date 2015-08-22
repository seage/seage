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

import java.util.List;

import org.seage.data.Pair;
import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.jssp.JsspPhenotypeEvaluator;
import org.seage.problem.jssp.Schedule;
import org.seage.problem.jssp.ScheduleCell;

/**
 * Summary description for JsspMoveManager.
 */
public class JsspMoveManager implements MoveManager
{
    private JsspPhenotypeEvaluator _evaluator;
    private int _maxMoves;

    public JsspMoveManager(JsspPhenotypeEvaluator evaluator)
    {
        _evaluator = evaluator;
        _maxMoves = 100;
    }

    @Override
    public Move[] getAllMoves(Solution solution) throws Exception
    {
        JsspSolution sol = (JsspSolution) solution;

        _evaluator.evaluateSchedule(sol.getJobArray(), true);

        Schedule schedule = _evaluator.getSchedule();
        List<Pair<ScheduleCell>> criticalPath = schedule.findCriticalPath();

        JsspMove[] moves = new JsspMove[Math.min(criticalPath.size() / 2, _maxMoves)];
        for (int i = 0; i < moves.length; i++)
        {
            moves[i] = new JsspMove(criticalPath.get(i).getFirst().getIndex(), criticalPath.get(i).getSecond().getIndex());
        }

        return moves;

    }
}
