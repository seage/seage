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
 * The <tt>TabuList</tt> tracks which moves
 * are tabu and for how long.
 *
 * @author Robert Harder
 * @see Move
 * @see Solution
 * @version 1.0
 * @since 1.0
 */
    
public interface TabuList extends java.io.Serializable
{

    /**
     * This method accepts a {@link Move} and {@link Solution} as
     * arguments and updates the tabu list as necessary.
     * <P>
     * Although the tabu list may not use both of the passed
     * arguments, both must be included in the definition.
     * 
     * @param move The {@link Move} to register
     * @param solution The {@link Solution} before the move operation
     * @see Move
     * @see Solution
     * @since 1.0
     */
    public abstract void setTabu( Solution fromSolution, Move move );
        
        
    /**
     * This function should be able to determine if
     * a given move is on the tabu list. The solution that is passed is the
     * solution before the move has operated on it.
     * This helps when you can incrementally evaluate your objective function even 
     * though it makes some hashing tabu lists a bit more difficult.
     * 
     * @param move A move
     * @param solution The solution before the move operation.
     * @return whether or not the tabu list permits the move.
     * @see Move
     * @see Solution
     * @since 1.0
     */
    public abstract boolean isTabu( Solution fromSolution, Move move );    
    
    
}   // end interface TabuList


