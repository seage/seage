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

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 * Summary description for JspMove.
 */
public class JspMove implements Move {
  private int ix1;
  private int ix2;

  public JspMove(int ix1, int ix2) {
    this.ix1 = ix1;
    this.ix2 = ix2;
  }

  @Override
  public void operateOn(Solution soln) {
    JspTabuSearchSolution solution = (JspTabuSearchSolution) soln;
    int tmp = solution.getJobArray()[ix1];
    solution.getJobArray()[ix1] = solution.getJobArray()[ix2];
    solution.getJobArray()[ix2] = tmp;
  }

  public int getIndex1() {
    return ix1;
  }

  public int getIndex2() {
    return ix2;
  }

  @Override
  public int hashCode() {
    return (ix1 << 10) + ix2 << 5;
  }

  @Override
  public boolean equals(Object jspMove) {
    return jspMove != null && this.hashCode() == jspMove.hashCode();
  }
}
