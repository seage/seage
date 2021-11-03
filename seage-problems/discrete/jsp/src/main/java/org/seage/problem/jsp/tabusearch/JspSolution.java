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
package org.seage.problem.jsp.tabusearch;

import org.seage.metaheuristic.tabusearch.SolutionAdapter;

/**
 * Summary description for JspSolution.
 */
public class JspSolution extends SolutionAdapter
{
  private Integer[] _scheduleArray;

  public JspSolution(Integer[] scheduleArray)
  {
    _scheduleArray = scheduleArray.clone();
  }

  public JspSolution(int numJobs, int numOpers)
  {
    _scheduleArray= new Integer[numJobs*numOpers];

    int i=0;
    for(int j=0;j<numJobs;j++)
      for(int k=0;k<numOpers;k++)
        _scheduleArray[i++] = j+1;
    
  }

  public Integer[] getScheduleArray()
  {
    return _scheduleArray;
  }

  @Override
  public String toString()
  {
    return getObjectiveValue()[0] + "";
  }

  @Override
  public Object clone()
  {
    JspSolution copy = (JspSolution) super.clone();
    copy._scheduleArray = this._scheduleArray.clone();
    return copy;
  } // end clone
}
