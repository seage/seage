/*******************************************************************************
 * Copyright (c) 2001 Robert Harder
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 */
package org.seage.metaheuristic.tabusearch;


/**
 * This exception is thrown when {@link MoveManager#getAllMoves}
 * (in the {@link MoveManager}returns no moves.
 * Execution does not stop. The {@link TabuSearch} moves
 * on to the next iteration and again requests
 * {@link MoveManager#getAllMoves} from the {@link MoveManager}.
 * 
 * @author Robert Harder
 * @see MoveManager
 * @see TabuSearch
 * @version 1.0
 * @since 1.0
 */
public class NoMovesGeneratedException extends java.lang.Exception
{
    
    /**
     * Constructs generic <tt>NoMovesGeneratedException</tt>.
     * This constructor only calls <code>super()</code> and quits.
     *
     * @since 1.0
     */
    public NoMovesGeneratedException()
    {   super();
    }   // end constructor
    
    
    /**
     * Constructs a <tt>NoMovesGeneratedException</tt> with
     * the specified {@link java.lang.String}. This constructor calls
     * <code>super( s )</code> and quits.
     *
     * @param s {@link String} describing the exception
     * @since 1.0
     **/
    public NoMovesGeneratedException( String s )
    {   super( s );
    }   // end constructor
    
    
    
}   // end NoMovesGeneratedException

