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

import java.util.function.Function;

/**
 * The <tt>TabuSearch</tt> is the main source of control
 * for the programmer. The extended and instantiated
 * tabu objects are passed to the tabu search, and the tabu search
 * fires off relevant events to interested listeners. These
 * events make it easy to extend the tabu search to include
 * popular techniques such as intensification, diversification,
 * and strategic oscillation.
 * <P>
 * As always, I am available for questions at
 * 
 * @author Robert Harder, rharder@usa.net
 * @version 1.0a
 * @since 1.0
 */
public interface ITabuSearch extends java.io.Serializable
{

    /* ********  C O N T R O L   M E T H O D S  ******** */

    /**
     * Starts the tabu search if <tt>iterationsToGo</tt> is greater than zero.
     *
     * @see #setIterationsToGo
     * @see #getIterationsToGo
     * @since 1.0
     */
    public abstract void startSolving() throws Exception;

    /**
     * Stops the tabu search after the current iteration finishes 
     * but leaves <tt>iterationsToGo</tt> untouched.
     *
     * <p>Implementations of <tt>TabuSearch</tt> should block on this method until the iteration is done.</p>
     *
     * @see #setIterationsToGo
     * @see #getIterationsToGo
     * @since 1.0
     */
    public abstract void stopSolving();

    /**
     * Returns <tt>true</tt> if the tabu search is currently solving.
     *
     * @return Whether or not the tabu search is currently solving.
     * @since 1.0
     */
    public abstract boolean isSolving();

    /* ********  L I S T E N E R   M E T H O D S  ******** */

    /**
     * Register a {@link TabuSearchListener} with the tabu search.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param listener The listener to register.
     * @see TabuSearchListener
     * @since 1.0
     */
    public abstract void addTabuSearchListener(TabuSearchListener listener);

    /**
     * Unregister a {@link TabuSearchListener} with the tabu search.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param listener The listener to unregister.
     * @see TabuSearchListener
     * @since 1.0
     */
    public abstract void removeTabuSearchListener(TabuSearchListener listener);

    /* ********  S E T   M E T H O D S  ******** */

    /**
     * Sets the objective function effective at the start of the next iteration
     * and re-evaluates the best and current solutions.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param function The new objective function
     * @see ObjectiveFunction
     * @since 1.0
     */
    public abstract void setObjectiveFunction(ObjectiveFunction function) throws Exception;

    /**
     * Sets the move manager effective at the start of the next iteration.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param moveManager The new move manager
     * @see MoveManager
     * @since 1.0
     */
    public abstract void setMoveManager(MoveManager moveManager);

    /**
     * Sets the tabu list effective at the start of the next iteration.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param tabuList The new tabu list
     * @see TabuList
     * @since 1.0
     */
    public abstract void setTabuList(TabuList tabuList);

    /**
     * Sets the aspiration critera effective at the start of the next iteration.
     * A <tt>null</tt> value indicates that no aspiration criteria is to be used.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param aspirationCriteria The new aspiration criteria
     * @see AspirationCriteria
     * @since 1.0
     */
    public abstract void setAspirationCriteria(AspirationCriteria aspirationCriteria);

    /**
     * Sets the best solution effective at the start of the next iteration
     * and evaluates the solution with the current objective function.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param solution The new best solution
     * @see Solution
     * @since 1.0
     */
    public abstract void setBestSolution(Solution solution);

    /**
     * Sets the current solution effective at the start of the next iteration
     * and evaluates the solution with the current objective function.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param solution The new current solution
     * @see Solution
     * @since 1.0
     */
    public abstract void setCurrentSolution(Solution solution) throws Exception;

    /**
     * Sets the iterations remaining to be solved. 
     * The number of iterations to go is decremented at the beginning
     * of each iteration. If a listener, which is notified at the end
     * of an iteration, increases <tt>iterationsToGo</tt> from zero to a
     * positive number, the tabu search will continue solving as if it never
     * was about to stop.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param iterations The number of iterations left to go
     * @since 1.0
     */
    public abstract void setIterationsToGo(int iterations);

    /**
     * Sets whether or not the tabu search should be maximizing the objective function.
     * A value of <tt>true</tt> means <em>maximize</em> while a value of <tt>false</tt>
     * means <em>minimize</em>.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @param maximizing <tt>true</tt> if the tabu search should be maximizing, <tt>false</tt> otherwise.
     * @since 1.0
     */
    public abstract void setMaximizing(boolean maximizing);

    /**
     * Setting this to <tt>true</tt> will cause the search to go faster
     * by not necessarily evaluating all of the moves in a neighborhood
     * for each iteration. Instead of evaluating all of the moves and
     * selecting the best one for execution, setting this will cause
     * the tabu search engine to select the first move that it encounters
     * that causes an improvement to the current solution.
     * The default value should be <tt>false</tt>.
     *
     * @param choose Whether or not the first improving move will be chosen
     * @since 1.0.1
     */
    public abstract void setChooseFirstImprovingMove(boolean choose);

    /**
     * Returns whether or not the tabu search engine will choose the
     * first improving move it encounters at each iteration (<tt>true</tt>)
     * or will choose the best move at each iteration (<tt>false</tt>).
     *
     * @return Whether or not the first improving move will be chosen
     * @since 1.0.1
     */
    public abstract boolean isChooseFirstImprovingMove();

    /* ********  G E T   M E T H O D S  ******** */

    /**
     * Returns the objective function.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return The objective function.
     * @see Function
     * @since 1.0
     */
    public abstract ObjectiveFunction getObjectiveFunction();

    /**
     * Returns the move manager.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return The move manager.
     * @see MoveManager
     * @since 1.0
     */
    public abstract MoveManager getMoveManager();

    /**
     * Returns the tabu list.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return The tabu list.
     * @see TabuList
     * @since 1.0
     */
    public abstract TabuList getTabuList();

    /**
     * Returns the aspiration critera.
     * A <tt>null</tt> value indicates that no aspiration criteria is to be used.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return The aspiration criteria
     * @see AspirationCriteria
     * @since 1.0
     */
    public abstract AspirationCriteria getAspirationCriteria();

    /**
     * Returns the best solution found so far.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return The best solution found so far.
     * @see Solution
     * @since 1.0
     */
    public abstract Solution getBestSolution();

    /**
     * Returns the current solution.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return The current solution.
     * @see Solution
     * @since 1.0
     */
    public abstract Solution getCurrentSolution();

    /**
     * Returns the number of iterations left to go.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return The number of iterations left to go.
     * @since 1.0
     */
    public abstract int getIterationsToGo();

    /**
     * Returns the total number iterations that have been
     * completed since the instantiation of this {@link TabuSearch}.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return Total number of completed iterations
     * @since 1.0a
     */
    public abstract int getIterationsCompleted();

    /**
     * Returns whether or not the tabu search should be maximizing the objective function.
     * A value of <tt>true</tt> means <em>maximize</em> while a value of <tt>false</tt>
     * means <em>minimize</em>.
     *
     * <p><em>Implementations of <tt>TabuSearch</tt> should synchronize this method.</em></p>
     *
     * @return Whether or not the tabu search should be maximizing the objective function.
     * @since 1.0
     */
    public abstract boolean isMaximizing();

} // end interface TabuSearch
