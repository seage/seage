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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * .
 * Contributors:
 *   Jan Zmatlik
 *   - Initial implementation
 *   David Omrai
 *   - Editation and bug fix
 */

package org.seage.problem.fsp.sannealing;

import java.util.Random;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.sannealing.JspObjectiveFunction;
import org.seage.problem.jsp.sannealing.JspSimulatedAnnealingSolution;

/**
 * Fsp move manager.
 *
 * @author David Omrai
 */
public class FspMoveManager implements IMoveManager {
  Random rnd = new Random();
  private int maxMoves;
  private JspJobsDefinition jobsDefinition;
  private JspObjectiveFunction objFunc;

  /**
   * .
   *
   * @param jobsDefinition definition of the jobs.
   * @param objFunc jsp objective function.
   */
  public FspMoveManager(JspJobsDefinition jobsDefinition, JspObjectiveFunction objFunc) {
    this.jobsDefinition = jobsDefinition;
    this.objFunc = objFunc;
    this.maxMoves = 100;
  }

  @Override
  public Solution getModifiedSolution(
      Solution solution, double currentTemperature) throws Exception {
    // return getModifiedBestSolution(solution, currentTemperature);
    return getModifiedRandomSolution(solution, currentTemperature);
  }

  private Solution getModifiedBestSolution(
      Solution solution, double currentTemperature) throws Exception {
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
      if (i == a) {
        continue;
      }
      move[1] = i;
      double val = this.objFunc.evaluate(jspSolution, move)[0];
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

  /**
   * .
   *
   * @param solution solution
   * @param currentTemperature current temperature
   * @return returns solution
   * @throws Exception .
   */
  public Solution getModifiedRandomSolution(
      Solution solution, double currentTemperature) throws Exception {
    JspSimulatedAnnealingSolution jspSolution = ((JspSimulatedAnnealingSolution) solution).clone();

    int jspSolutionLength = jspSolution.getJobArray().length;
    int a = rnd.nextInt(jspSolutionLength);
    int b = rnd.nextInt(jspSolutionLength);

    if (a == b) {
      b = (b + 1) % jspSolutionLength;
    }  
    int tmp = jspSolution.getJobArray()[a];
    jspSolution.getJobArray()[a] = jspSolution.getJobArray()[b];
    jspSolution.getJobArray()[b] = tmp;
    
    return jspSolution;
  }
}