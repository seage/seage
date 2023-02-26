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
package org.seage.problem.fsp.tabusearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seage.data.Pair;
import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.Schedule;
import org.seage.problem.jsp.ScheduleCell;
import org.seage.problem.jsp.tabusearch.JspMove;
import org.seage.problem.jsp.tabusearch.JspTabuSearchSolution;

/**
 * Summary description for JspMoveManager.
 */
public class FspMoveManager implements MoveManager {
  private int _maxMoves;
  private JspJobsDefinition _jobsDefinition;
  private Random rnd;

  public FspMoveManager(JspJobsDefinition jobsDefinition) {
    _jobsDefinition = jobsDefinition;
    _maxMoves = 100;
    this.rnd = new Random();
  }

  // @Override
  public Move[] getAllMoves0(Solution solution) throws Exception {
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

  @Override
  public Move[] getAllMoves(Solution solution) {
    JspTabuSearchSolution sol = (JspTabuSearchSolution) solution;
    //Schedule schedule = new Schedule(_jobsDefinition, sol.getJobArray());
    List<Move> moves = new ArrayList<>();

    int ix1 = rnd.nextInt(sol.getJobArray().length);

    // Generate moves that move each customer
    // forward and back up to five spaces.
    for (int i = 0; i < sol.getJobArray().length; i++) {
      int ix2 = (ix1 + i) % sol.getJobArray().length;
      if (ix1 == ix2)
        continue;
      moves.add(new JspMove(ix1, ix2));
    }

    return moves.toArray(new Move[] {});
  }
}
