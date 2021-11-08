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
 *   Jan Zmatlik
 *   - Initial implementation
 *   David Omrai
 *   - Editation and bug fix
 */
package org.seage.problem.jsp.sannealing;

import java.util.List;
import java.util.Random;

import org.seage.data.Pair;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.Schedule;
import org.seage.problem.jsp.ScheduleCell;

/**
 *
 * @author Jan Zmatlik
 * Edited by David Omrai
 */
public class JspMoveManager implements IMoveManager
{
  Random rnd = new Random();
  private int _maxMoves;
  private JobsDefinition _jobsDefinition;
  private JspObjectiveFunction _objFunc;

  public JspMoveManager(JobsDefinition jobsDefinition, JspObjectiveFunction objFunc) {
    _jobsDefinition = jobsDefinition;
    _objFunc = objFunc;
    _maxMoves = 100;
  }

  @Override
  public Solution getModifiedSolution(Solution solution, double ct) throws Exception
  {
    //return getModifiedBestSolution(solution, ct);
    //return getModifiedRandomSolution(solution, ct);
    return getModifiedCriticalPathSolution(solution, ct);
  }

  private Solution getModifiedCriticalPathSolution(Solution solution, double ct) throws Exception {
    JspSimulatedAnnealingSolution jspSolution = (JspSimulatedAnnealingSolution) solution.clone();
    Schedule schedule = new Schedule(_jobsDefinition, jspSolution.getJobArray());
    // Find critical path
    List<Pair<ScheduleCell>> criticalPath = schedule.findCriticalPath();

    int[] bestMove = new int[2];
    double bestVal = Double.MAX_VALUE;

    int[] move = new int[2];

    // Find the best move
    for (int i = 0; i < criticalPath.size(); i++) {
      move[0] = criticalPath.get(i).getFirst().getIndex();
      move[1] = criticalPath.get(i).getSecond().getIndex();
      double tmpVal = _objFunc.evaluate(jspSolution, move)[0];

      if (tmpVal < bestVal) {
        bestVal = tmpVal;
        bestMove = move.clone();
      }
    }

    // Apply the best move
    int tmp = jspSolution.getJobArray()[bestMove[0]];
    jspSolution.getJobArray()[bestMove[0]] = jspSolution.getJobArray()[bestMove[1]];
    jspSolution.getJobArray()[bestMove[1]] = tmp;

    return jspSolution;
  }

  private Solution getModifiedBestSolution(Solution solution, double ct) throws Exception {
    JspSimulatedAnnealingSolution jspSolution = ((JspSimulatedAnnealingSolution) solution).clone();

    int jspSolutionLength = jspSolution.getJobArray().length;
    int a = rnd.nextInt(jspSolutionLength);
    
    int[] move = new int[2];
    move[0] = a;
    move[1] = 0;
    
    // Set the value to maximal, if none is better best move is not set
    double bestVal = Double.MAX_VALUE;
    int[] bestMove = null;

    for (int i = 0; i < jspSolutionLength; i++) {
      if (i == a)
        continue;
      move[1] = i;
      double val = _objFunc.evaluate(jspSolution, move)[0];
      if (val < bestVal) {
        bestVal = val;
        bestMove = move.clone();
      }
    }

    if (bestMove != null) {
      int tmp = jspSolution.getJobArray()[bestMove[0]];
      jspSolution.getJobArray()[bestMove[0]] = jspSolution.getJobArray()[bestMove[1]];
      jspSolution.getJobArray()[bestMove[1]] = tmp;
    }   

    return jspSolution;
  }

  public Solution getModifiedRandomSolution(Solution solution, double ct) throws Exception {
    JspSimulatedAnnealingSolution jspSolution = ((JspSimulatedAnnealingSolution) solution).clone();

    int jspSolutionLength = jspSolution.getJobArray().length;
    int a = rnd.nextInt(jspSolutionLength);
    int b = rnd.nextInt(jspSolutionLength);

    if (a == b) 
      b = (b + 1) % jspSolutionLength;
  
      int tmp = jspSolution.getJobArray()[a];
      jspSolution.getJobArray()[a] = jspSolution.getJobArray()[b];
      jspSolution.getJobArray()[b] = tmp;
    
    return jspSolution;
  }

}
