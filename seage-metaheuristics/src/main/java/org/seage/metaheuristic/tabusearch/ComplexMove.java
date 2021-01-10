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
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 */
package org.seage.metaheuristic.tabusearch;

/**
 * This extends the {@link Move} interface and requires you to implement a
 * <code>public int[] attributes()</code> method that returns attributes of that
 * move. The length of the array is the number of attributes you are tracking in
 * your {@link ComplexTabuList}.
 *
 * @author Robert Harder
 * @see ObjectiveFunction
 * @see Solution
 * @version 1.0
 * @since 1.0-exp9
 */

public interface ComplexMove extends Move {

  /**
   * Returns an array of attributes to track in the {@link ComplexTabuList}.
   *
   * @return array of attributes
   * @since 1.0-exp9
   */
  public abstract int[] attributes();

} // end class ComplexMove
