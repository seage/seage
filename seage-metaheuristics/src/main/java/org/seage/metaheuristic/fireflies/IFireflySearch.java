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
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

import org.seage.metaheuristic.fireflies.*;

/**
 * The <tt>FireFly</tt> is the main source of control
 * for the programmer. The extended and instantiated
 * firefly objects are passed to the firefly search, and the firefly search
 * fires off relevant events to interested listeners. These
 * events make it easy to extend the firefly search to include
 * popular techniques such as intensification, diversification,
 * and strategic oscillation.
 */
public interface IFireflySearch extends java.io.Serializable
{
    
/* ********  C O N T R O L   M E T H O D S  ******** */
    
    
    
    /**
     * Starts the firefly search if <tt>iterationsToGo</tt> is greater than zero.
     *
     * @see #setIterationsToGo
     * @see #getIterationsToGo
     * @since 1.0
     */
    public abstract void startSolving(Solution[] solutions) throws Exception;
    
    
    
    /**
     * Stops the firefly search after the current iteration finishes
     * but leaves <tt>iterationsToGo</tt> untouched.
     *
     * <p>Implementations of <tt>FireflySearch</tt> should block on this method until the iteration is done.</p>
     *
     * @see #setIterationsToGo
     * @see #getIterationsToGo
     * @since 1.0
     */
    public abstract void stopSolving();
    
    
    /**
     * Returns <tt>true</tt> if the firefly search is currently solving.
     *
     * @return Whether or not the firefly search is currently solving.
     * @since 1.0
     */
    public abstract boolean isSolving();
    
    
    
/* ********  L I S T E N E R   M E T H O D S  ******** */
    
    
    
    /**
     * Register a {@link FireflySearchListener} with the firefly search.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @param listener The listener to register.
     * @see FireflySearchListener
     * @since 1.0
     */
    public abstract void addFireflySearchListener( FireflySearchListener listener );
    
    
    
    /**
     * Unregister a {@link FireflySearchListener} with the firefly search.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @param listener The listener to unregister.
     * @see FireflySearchListener
     * @since 1.0
     */
    public abstract void removeFireflySearchListener( FireflySearchListener listener );
    
    
    
    
    
/* ********  S E T   M E T H O D S  ******** */    
    
    
    
    /**
     * Sets the objective function effective at the start of the next iteration
     * and re-evaluates the best and current solutions.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @param function The new objective function
     * @see ObjectiveFunction
     * @since 1.0
     */
    public abstract void setObjectiveFunction(ObjectiveFunction function) throws Exception;
    
    
    
    /**
     * Sets the best solution effective at the start of the next iteration
     * and evaluates the solution with the current objective function.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @param solution The new best solution
     * @see Solution
     * @since 1.0
     */
    public abstract void setBestSolution( Solution solution );
    
    /**
     * Sets the iterations remaining to be solved. 
     * The number of iterations to go is decremented at the beginning
     * of each iteration. If a listener, which is notified at the end
     * of an iteration, increases <tt>iterationsToGo</tt> from zero to a
     * positive number, the firefly search will continue solving as if it never
     * was about to stop.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @param iterations The number of iterations left to go
     * @since 1.0
     */
    public abstract void setIterationsToGo( int iterations );
        
    /**
     * Sets whether or not the firefly search should be maximizing the objective function.
     * A value of <tt>true</tt> means <em>maximize</em> while a value of <tt>false</tt>
     * means <em>minimize</em>.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @param maximizing <tt>true</tt> if the firefly search should be maximizing, <tt>false</tt> otherwise.
     * @since 1.0
     */
    public abstract void setMaximizing( boolean maximizing );   
    
    
/* ********  G E T   M E T H O D S  ******** */    
    
    
    
    
    /**
     * Returns the objective function.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @return The objective function.
     * @see Function
     * @since 1.0
     */
    public abstract ObjectiveFunction getObjectiveFunction();
    
    
    /**
     * Returns the best solution found so far.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @return The best solution found so far.
     * @see Solution
     * @since 1.0
     */
    public abstract Solution getBestSolution();
    
    
    /**
     * Returns the number of iterations left to go.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @return The number of iterations left to go.
     * @since 1.0
     */
    public abstract int getIterationsToGo();
    

    public abstract void setCurrentIteration(int _currentIteration);

    public abstract int getCurrentIteration();

    
    /**
     * Returns the total number iterations that have been
     * completed since the instantiation of this {@link FireflySearch}.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @return Total number of completed iterations
     * @since 1.0a
     */
    public abstract int getIterationsCompleted();
    
    
    
    /**
     * Returns whether or not the firefly search should be maximizing the objective function.
     * A value of <tt>true</tt> means <em>maximize</em> while a value of <tt>false</tt>
     * means <em>minimize</em>.
     *
     * <p><em>Implementations of <tt>FireflySearch</tt> should synchronize this method.</em></p>
     *
     * @return Whether or not the firefly search should be maximizing the objective function.
     * @since 1.0
     */
    public abstract boolean isMaximizing();
    
    
}   // end interface FireflySearch
