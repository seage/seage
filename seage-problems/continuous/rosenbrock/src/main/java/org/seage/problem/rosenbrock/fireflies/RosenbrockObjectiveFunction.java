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
 */

package org.seage.problem.rosenbrock.fireflies;

import org.seage.metaheuristic.fireflies.ObjectiveFunction;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.problem.rosenbrock.RosenbrockFunction;

/**
 * .
 *
 * @author Jan Zmatlik
 */
public class RosenbrockObjectiveFunction implements ObjectiveFunction {
      
  public int counter = 0;
      
  // f(X) = SUM(to N-1, i = 0) [(1 - Xi)^2 + 100 * (Xi+1 - Xi^2)^2]
  public void setObjectiveValue(Solution sol) {
    Double[] coords = ((ContinuousSolution) sol).assign;
    sol.setObjectiveValue(new double[]{ RosenbrockFunction.f(coords)});
  }

  public double[] evaluate(Solution soln) throws Exception {
    Double[] coords = ((ContinuousSolution) soln).assign;    
    return new double[]{ RosenbrockFunction.f(coords) };
  }

  public int getCounter() {
    return counter;
  }

  public void incrementCounter() {
    counter++;
  }
}
