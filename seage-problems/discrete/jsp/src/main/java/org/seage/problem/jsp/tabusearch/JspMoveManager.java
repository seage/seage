/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */
package org.seage.problem.jsp.tabusearch;

import java.util.List;

import org.seage.data.Pair;
import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.Schedule;
import org.seage.problem.jsp.ScheduleCell;

/**
 * Summary description for JspMoveManager.
 */
public class JspMoveManager implements MoveManager {
  private int _maxMoves;
  private JspJobsDefinition _jobsDefinition;

  public JspMoveManager(JspJobsDefinition jobsDefinition) {
    _jobsDefinition = jobsDefinition;
    _maxMoves = 100;
  }

  @Override
  public Move[] getAllMoves(Solution solution) throws Exception {
    JspTabuSearchSolution sol = (JspTabuSearchSolution) solution;
    Schedule schedule = new Schedule(_jobsDefinition, sol.getJobArray());

    List<Pair<ScheduleCell>> criticalPath = schedule.findCriticalPath();

    JspMove[] moves = new JspMove[Math.min(criticalPath.size(), _maxMoves)];
    for (int i = 0; i < moves.length; i++) {
      moves[i] = new JspMove(criticalPath.get(i).getFirst().getIndex(),
          criticalPath.get(i).getSecond().getIndex());
    }

    return moves;
  }
}
