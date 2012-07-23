/*******************************************************************************
 * Copyright (c) 2001 Robert Harder
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
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

