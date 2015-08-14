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
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

/**
 * An object that will listen for the firefly search to start.
 * 
 * @author Robert Harder
 * @see FireflySearch
 * @see FireflySearchEvent
 * @version 1.0
 * @since 1.0
 */
public interface FireflySearchListener extends java.util.EventListener
{

    /**
     * Called when the firefly search has started.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that started.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void FireflySearchStarted(FireflySearchEvent e);

    /**
     * Called when the firefly search stops solving, either because all requested
     * iterations are completed or someone has called 
     * {@link FireflySearch#stopSolving myFireflySearch.stopSolving()}.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that stopped.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void FireflySearchStopped(FireflySearchEvent e);

    /**
     * Called when a new best solution is found.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that found
     * the new best solution.
     * @see FireflySearchEvent
     * @since 1.0
     */
    public void newBestSolutionFound(FireflySearchEvent e);

    /**
     * Called when a new current solution is found, which, by defintion,
     * is at every iteration, even if your "move" doesn't perceptibly
     * alter the solution.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that found
     * a new current solution.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void newCurrentSolutionFound(FireflySearchEvent e);

    /**
     * Called when the firefly search makes an unimproving move.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that made
     * the unimproving move.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0
     */
    public void unimprovingMoveMade(FireflySearchEvent e);

    /**
     * Called when the firefly search makes an improving move.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that made
     * the improving move.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0-exp7
     */
    public void improvingMoveMade(FireflySearchEvent e);

    /**
     * Called when the firefly search makes a no change in value move.
     * 
     * @param e The {@link FireflySearchEvent} referencing the {@link FireflySearch} that made
     * the no change in solution value move.
     * @see FireflySearchEvent
     * @see FireflySearch
     * @since 1.0-exp7
     */
    public void noChangeInValueMoveMade(FireflySearchEvent e);

} // end interface FireflySearchListener
