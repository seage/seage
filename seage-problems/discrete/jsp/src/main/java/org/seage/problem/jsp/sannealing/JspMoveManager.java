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

import java.util.Random;

import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.jsp.JobsDefinition;

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

  public JspMoveManager(JobsDefinition jobsDefinition) {
    _jobsDefinition = jobsDefinition;
    _maxMoves = 100;
  }

  @Override
  public Solution getModifiedSolution(Solution solution, double ct)
  {
    JspSimulatedAnnealingSolution jspSolution = ((JspSimulatedAnnealingSolution) solution).clone();

    int jspSolutionLength = jspSolution.getJobArray().length;
    int a = rnd.nextInt(jspSolutionLength);
    int b = rnd.nextInt(jspSolutionLength);

    // Change values to be different
    if (a == b)
      a = (a + 1) % jspSolutionLength; 

    // Swap values
    int tmp = jspSolution.getJobArray()[a];
    jspSolution.getJobArray()[a] = jspSolution.getJobArray()[b];
    jspSolution.getJobArray()[b] = tmp;
    

    return jspSolution;
  }

}
