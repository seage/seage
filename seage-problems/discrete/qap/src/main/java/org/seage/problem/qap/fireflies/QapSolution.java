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
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.qap.fireflies;

import org.seage.metaheuristic.fireflies.SolutionAdapter;

/**
 *
 * @author Karel Durkota
 */
public class QapSolution extends SolutionAdapter {
  protected Integer[] assign;

  public QapSolution() {} // Appease clone()

  public QapSolution(Double[][][] customers) {
    // Crudely initialize solution
    assign = new Integer[customers.length];
    for (int i = 0; i < customers.length; i++) {
      assign[i] = i;
    }
  } // end constructor

  public QapSolution(Integer[] assign) {
    this.assign = assign;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    QapSolution copy = (QapSolution) super.clone();
    copy.assign = this.assign.clone();
    return copy;
  } // end clone

  public Integer[] getAssign() {
    return assign;
  }

  public void setAssign(Integer[] assign) {
    this.assign = assign;
  }

  @Override
  public String toString() {
    StringBuffer s = new StringBuffer();

    s.append("[");
    for (int i = 0; i < assign.length - 1; i++) {
      s.append((assign[i] + 1));
      s.append(",");
    }
    s.append((assign[assign.length - 1] + 1) + "]");

    return s.toString();
  } // end toString

  @Override
  public boolean equals(Object in) {
    if (!(in instanceof QapSolution)) {
      return false;
    }
    QapSolution q = (QapSolution) in;
    for (int i = 0; i < assign.length; i++) {
      if (!assign[i].equals(q.getAssign()[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return 0; // TODO
  }

} // end class MySolution
