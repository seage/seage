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
 *   - Implemented for jsp problem
 */
package org.seage.problem.jsp.sannealing;


import org.seage.metaheuristic.sannealing.Solution;

/**
 *
 * @author Jan Zmatlik
 * Edited by David Omrai
 */
public abstract class JspSolution extends Solution
{

  /**
   * Represent order of operations
   */
  protected Integer[] _schedule;

  /**
   * Array of operations
   */

  public JspSolution(int length)
  {
    _schedule = new Integer[length];
  }

  public Integer[] getSchedule()
  {
    return _schedule;
  }

  public void setSchedule(Integer[] schedule)
  {
    _schedule = schedule;
  }

  @Override
  public String toString()
  {
    String res = new String();
    for (Integer t : _schedule)
      res += t + " ";
    return res;
  }

  @Override
  public JspSolution clone()
  {
    JspSolution jspSolution = null;

    jspSolution = (JspSolution) super.clone();
    jspSolution.setSchedule(_schedule.clone());
    jspSolution.setObjectiveValue(getObjectiveValue());

    return jspSolution;
  }

}
