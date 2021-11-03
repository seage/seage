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

/**
 *
 * @author Jan Zmatlik
 */
public class JspMoveManager implements IMoveManager
{
  Random rnd = new Random();

  @Override
  public Solution getModifiedSolution(Solution solution, double ct)
  {
    JspSolution jspSolution = ((JspSolution) solution).clone();

    int jspSolutionLength = jspSolution.getSchedule().length;
    int a = rnd.nextInt(jspSolutionLength);
    int b = rnd.nextInt(jspSolutionLength);

    // Swap values if indices are different
    if (a != b)
    {
      int tmp = jspSolution.getSchedule()[a];
      jspSolution.getSchedule()[a] = jspSolution.getSchedule()[b];
      jspSolution.getSchedule()[b] = tmp;
    }

    return jspSolution;
  }

}
