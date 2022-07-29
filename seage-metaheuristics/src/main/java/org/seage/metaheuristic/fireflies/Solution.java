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
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 */

package org.seage.metaheuristic.fireflies;

/**
 *
 * @author Karel Durkota
 */
public interface Solution extends Cloneable {

  /**
   * If the value has been set for this solution, then the value will be returned.
   * Be careful if this returns null since this may indicate that the
   * {@link FireflySearch} has not yet set the solution's value.
   *
   * @return The value of the solution
   * @since 1.0
   */
  public abstract double[] getObjectiveValue();

  /**
   * Generally used by the {@link FireflySearch} to set the value of the objective
   * function and set <code>objectiveValid</code> to <code>true</code>.
   *
   * @param objValue The objective function value
   * @since 1.0
   */
  public abstract void setObjectiveValue(double[] objValue);

  Object clone() throws CloneNotSupportedException;
  
}
