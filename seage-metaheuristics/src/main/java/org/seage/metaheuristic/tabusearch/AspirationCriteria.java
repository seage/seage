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
 * The optional aspiration criteria allows you to override a move's tabu status
 * if it offers something good such as resulting in a new best solution.
 * A <tt>null</tt> value implies no aspiration criteria.
 *
 * @author Robert Harder
 * @version 1.0
 * @since 1.0
 */        
public interface AspirationCriteria extends java.io.Serializable
{   

    /**
     * Determine if the proposed tabu move should in fact be allowed
     * because of some aspiration criteria such as being better than
     * the previously-known best solution. 
     * The proposed move, the solution it would operate on, and
     * the resulting objective function value are passed along with the
     * {@link TabuSearch} itself in case there's some other data you want.
     *
     * @param soln The solution to be modified
     * @param move The proposed move
     * @param value The resulting objective function value
     * @param tabuSearch The {@link TabuSearch} controlling the transaction
     * @see Solution
     * @see Move
     * @see TabuSearch
     * @since 1.0
     */
    public abstract boolean overrideTabu( 
        Solution baseSolution, 
        Move proposedMove, 
        double[] proposedValue, 
        ITabuSearch tabuSearch );

                   
}   // end class AspirationCritera

