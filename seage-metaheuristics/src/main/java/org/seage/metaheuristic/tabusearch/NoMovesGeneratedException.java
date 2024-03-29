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
 *     Richard Malek
 *     - Merge with SEAGE
 */
package org.seage.metaheuristic.tabusearch;

/**
 * This exception is thrown when {@link MoveManager#getAllMoves} (in the
 * {@link MoveManager}returns no moves. Execution does not stop. The
 * {@link TabuSearch} moves on to the next iteration and again requests
 * {@link MoveManager#getAllMoves} from the {@link MoveManager}.
 * 
 * @author Robert Harder
 * @see MoveManager
 * @see TabuSearch
 * @version 1.0
 * @since 1.0
 */
public class NoMovesGeneratedException extends java.lang.Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 4796075708422074369L;

  /**
   * Constructs generic <code>NoMovesGeneratedException</code>. This constructor only
   * calls <code>super()</code> and quits.
   *
   * @since 1.0
   */
  public NoMovesGeneratedException() {
    super();
  } // end constructor

  /**
   * Constructs a <code>NoMovesGeneratedException</code> with the specified
   * {@link java.lang.String}. This constructor calls <code>super( s )</code> and
   * quits.
   *
   * @param s {@link String} describing the exception
   * @since 1.0
   **/
  public NoMovesGeneratedException(String s) {
    super(s);
  } // end constructor

} // end NoMovesGeneratedException
