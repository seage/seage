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
 *     Victor Wiley
 *     - Initial implementation
 *     Robert Harder
 *     - ???
 *     Richard Malek
 *     - Merge with SEAGE
 */
package org.seage.metaheuristic.tabusearch;

/**
 * Determine if the proposed tabu move should in fact be allowed because it
 * results in a value better than the current best solution's value. This is the
 * most common aspiration criteria used in tabu search. <br />
 * Thanks to Victor Wiley from the University of Texas at Austin for writing the
 * first draft of this class.
 *
 * @see AspirationCriteria
 * @see Solution
 * @see Move
 * @see TabuSearch
 * @since 1.0.2
 * @author Victor Wiley
 * @author Robert Harder
 */
public class BestEverAspirationCriteria implements AspirationCriteria {
  /**
   * 
   */
  private static final long serialVersionUID = 8050033847751573251L;

  /**
   * Determine if the proposed tabu move should in fact be allowed because it
   * results in a value better than the current best solution's value. This is the
   * most common aspiration criteria used in tabu search. <br />
   * Thanks to Victor Wiley from the University of Texas at Austin for writing the
   * first draft of this class.
   *
   * @param soln       The solution to be modified
   * @param move       The proposed move
   * @param value      The resulting objective function value
   * @param tabuSearch The {@link TabuSearch} controlling the transaction
   * @see AspirationCriteria
   * @see Solution
   * @see Move
   * @see TabuSearch
   * @since 1.0.1
   * @author Victor Wiley
   * @author Robert Harder
   */
  @Override
  public boolean overrideTabu(final Solution solution, final Move proposedMove, final double[] proposedValue,
      final ITabuSearch tabuSearch) {
    return
    // comparator.compare(solution, tabuSearch.getBestSolution()) == 1 ? true :
    // false;
    TabuSearch.isFirstBetterThanSecond(proposedValue, tabuSearch.getBestSolution().getObjectiveValue(),
        tabuSearch.isMaximizing()) ? true : false;
  } // end overrideTabu

} // end class BestEverAspirationCriteria
