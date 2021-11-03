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
public class JspSolution extends Solution
{

  /**
   * Represent order of operations
   */
  public Integer[] _jobArray;

  /**
   * Array of operations
   */
  public JspSolution(int numJobs, int numOpers)
  {
    _jobArray = new Integer[numJobs];

    int i=0;
    for(int j=0;j<numJobs;j++)
      for(int k=0;k<numOpers;k++)
        _jobArray[i++] = j+1;
  }

  /**
   * 
   */
  public JspSolution(Integer[] schedule) {
    _jobArray = schedule.clone();
  }

  public Integer[] getJobArray()
  {
    return _jobArray;
  }

  public void setSchedule(Integer[] schedule)
  {
    _jobArray = schedule.clone();
  }

  @Override
  public String toString()
  {
    String res = new String();
    for (Integer t : _jobArray)
      res += t + " ";
    return res;
  }

  @Override
  public JspSolution clone()
  {
    JspSolution jspSolution = null;

    jspSolution = (JspSolution) super.clone();
    jspSolution.setSchedule(_jobArray.clone());
    jspSolution.setObjectiveValue(getObjectiveValue());

    return jspSolution;
  }

}
