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
 * This exception is thrown when there is no current solution
 * Execution does not stop. The {@link TabuSearch} moves
 * on to the next iteration, giving other threads the opportunity
 * to set the current solution.
 * 
 * @author Robert Harder, rharder@usa.net
 * @version 1.0
 * @since 1.0
 */
public class NoCurrentSolutionException extends java.lang.Exception
{

    /**
     * Constructs generic <tt>NoCurrentSolutionException</tt>.
     * This constructor only calls <tt>super()</tt>.
     *
     * @since 1.0
     */
    public NoCurrentSolutionException()
    {   super();
    }   // end constructor
    
    
    /**
     * Constructs a <tt>NoCurrentSolutionException</tt> with
     * the specified {@link java.lang.String}. This constructor calls
     * <tt>super( s )</tt>.
     *
     * @param s <code>String</code> describing the exception
     * @since 1.0
     */
    public NoCurrentSolutionException(String s)
    {   super( s );
    }   // end constructor
    
    
}   // end NoCurrentSolutionException

