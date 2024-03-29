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
 * The objective function is used to evaluate a {@link Solution}.
 * 
 * @author Robert Harder
 * @version 1.0
 * @since 1.0
 */
public interface ObjectiveFunction {

  /**
   * The {@link TabuSearch} expects an objective/ constraint penalties function to
   * be able to evaluate a solution's worth. The {@link TabuSearch} will pass the
   * <code>ObjectiveFunction</code> a {@link Solution} and optionally pass a proposed
   * {@link Move}. If the passed move is not <code>null</code>, then the evaluation
   * should consider the effect that executing this move on the solution would
   * have before evaluating it. If you can use some sort of incremental evaluation
   * technique, this will save you time. If you must operate on the solution to
   * actually calculate its value, you must return the solution to its original
   * state before the method returns.
   * <P>
   * Casting example:<BR>
   * 
   * <pre>
   * <code>
   *     public double[] evaluate( Solution soln, Move move )<BR>
   *     {
   *         MySolutionClass solution = (MySolutionClass) soln;
   *         // ...
   *
   *     }   // end evaluate
   * </code>
   * </pre>
   * <P>
   * The array of returned values will later be compared lexicographically in the
   * classic "goal-programming" style. If instead you want some goals to overpower
   * higher goals, use the style of weighting the levels with appropriate values.
   * Although all numbers are stored and calculated as <code>double</code>s, they are
   * cast to <code>float</code>s before being compared.
   * <P>
   * Be careful that you do not "reuse" arrays which can lead to different
   * solutions sharing the same array of values in memory. A good technique is to
   * evaluate your values as scalars and then return a fresh array like this:
   * <P>
   * 
   * <pre>
   * <code>
   *     double val1 = ...;
   *     double val2 = ...;
   *     ...
   *     return new double[]{ val1, val2 };
   * </code>
   * </pre>
   *
   * 
   * @param soln The solution to evaluate
   * @param move If not <code>null</code> the proposed move
   * @return The function's value.
   * @see Solution
   * @see Move
   * @since 1.0
   */
  public abstract double[] evaluate(Solution soln, Move move) throws Exception;

} // end class ObjectiveFunction
