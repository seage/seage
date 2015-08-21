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
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.jssp.Schedule;
import org.seage.problem.jssp.ScheduleCell;
import org.seage.problem.jssp._old.ScheduleManager;

/**
 * Summary description for JsspMoveManager.
 */
public class JsspMoveManager implements MoveManager
{
    private ScheduleManager _scheduleManager;
    private int _maxMoves;

    public JsspMoveManager(ScheduleManager scheduleManager)
    {
        _scheduleManager = scheduleManager;
        _maxMoves = 100;
    }

    @Override
    public Move[] getAllMoves(Solution solution) throws Exception
    {
        try
        {
            JsspSolution sol = (JsspSolution) solution;

            Object[] val = _scheduleManager.evaluateSchedule(sol.getJobArray(), true);

            Schedule schedule = (Schedule) val[1];
            ScheduleCell[] criticalPath = schedule.findCriticalPath();

            JsspMove[] moves = new JsspMove[Math.min(criticalPath.length / 2, _maxMoves)];
            for (int i = 0; i < moves.length; i++)
            {
                moves[i] = new JsspMove(criticalPath[2 * i].getIndex(), criticalPath[2 * i + 1].getIndex());
            }

            return moves;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
