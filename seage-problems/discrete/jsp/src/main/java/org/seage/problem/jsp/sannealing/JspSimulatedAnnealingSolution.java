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
public class JspSimulatedAnnealingSolution extends Solution
{

  /**
   * Represent order of operations
   */
  private Integer[] jobArray;

  /**
   * Array of operations
   */
  public JspSimulatedAnnealingSolution(int numJobs, int numOpers)
  {
    this.jobArray = new Integer[numJobs];

    int i=0;
    for(int j=0;j<numJobs;j++)
      for(int k=0;k<numOpers;k++)
        this.jobArray[i++] = j+1;
  }

  /**
   * 
   */
  public JspSimulatedAnnealingSolution(Integer[] schedule) {
    this.jobArray = schedule.clone();
  }

  public Integer[] getJobArray()
  {
    return this.jobArray;
  }

  public void setSchedule(Integer[] schedule)
  {
    this.jobArray = schedule.clone();
  }

  @Override
  public String toString()
  {
    String res = new String();
    for (Integer t : this.jobArray)
      res += t + " ";
    return res;
  }

  @Override
  public JspSimulatedAnnealingSolution clone()
  {
    JspSimulatedAnnealingSolution jspSolution = null;

    jspSolution = (JspSimulatedAnnealingSolution) super.clone();
    jspSolution.setSchedule(this.jobArray.clone());
    jspSolution.setObjectiveValue(getObjectiveValue());

    return jspSolution;
  }

}
