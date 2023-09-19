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
 *   Richard Malek
 *   - Initial implementation
 */

package org.seage.problem.rosenbrock.fireflies;

import java.util.Arrays;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.metaheuristic.fireflies.SolutionAdapter;
import org.seage.problem.rosenbrock.sannealing.RosenbrockSolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * .

 * @author Administrator
 */
public class ContinuousSolution extends SolutionAdapter {
  private static final Logger logger = 
      LoggerFactory.getLogger(ContinuousSolution.class.getName());

  protected Double[] assign;
      
  public ContinuousSolution(){} // Appease clone()

  /**
   * .
   *
   * @param dim .
   * @param maxBound .
   * @param minBound .
   */
  public ContinuousSolution(int dim, double[] maxBound, double[] minBound){
    this.assign = new Double[dim];
    // for(int i=0;i<dim;i++){
    // }
  }
      
  public ContinuousSolution(Double[] assign){
    this.assign = assign;
  }

  /**
   * .
   */
  @Override
  public Object clone() {
    try {
      ContinuousSolution copy = (ContinuousSolution) super.clone();
      copy.assign = this.assign.clone();
      return copy;
    } catch (Exception ex) {
      logger.error("{}", ex.getMessage());
      return new ContinuousSolution();
    }
  }   // end clone

  public Double[] getAssign() {
    return assign;
  }

  public void setAssign(Double[] assign) {
    this.assign = assign;
  }
      
  /**
   * .
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();

    s.append("[");
    for (int i = 0; i < assign.length - 1; i++) {
      s.append((assign[i] + 1));
      s.append(",");
    }
    s.append((assign[assign.length - 1] + 1) + "]");
    
    return s.toString();
  }   // end toString

  /**
   * .
   */
  @Override
  public boolean equals(Object in) {
    ContinuousSolution q = (ContinuousSolution) in;
    if (q == null) {
      return false;
    }
    for (int i = 0; i < assign.length; i++) {
      if (assign[i] != q.getAssign()[i]) {
        return false;
      }
    }
    return true;
  }

  /**
   * .
   */
  @Override
  public int hashCode() {
    return Arrays.hashCode(this.assign);
  }
      
}
