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
 * This exception is thrown when there is no current solution Execution does not
 * stop. The {@link TabuSearch} moves on to the next iteration, giving other
 * threads the opportunity to set the current solution.
 * 
 * @author Robert Harder, rharder@usa.net
 * @version 1.0
 * @since 1.0
 */
public class NoCurrentSolutionException extends java.lang.Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 9040891795653606861L;

  /**
   * Constructs generic <tt>NoCurrentSolutionException</tt>. This constructor only
   * calls <tt>super()</tt>.
   *
   * @since 1.0
   */
  public NoCurrentSolutionException() {
    super();
  } // end constructor

  /**
   * Constructs a <tt>NoCurrentSolutionException</tt> with the specified
   * {@link java.lang.String}. This constructor calls <tt>super( s )</tt>.
   *
   * @param s <code>String</code> describing the exception
   * @since 1.0
   */
  public NoCurrentSolutionException(String s) {
    super(s);
  } // end constructor

} // end NoCurrentSolutionException
