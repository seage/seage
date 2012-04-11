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
 * The <tt>MoveManager</tt> determines which moves are available
 * at any given time (or given solution). As a performance boost, you
 * may want to consider reusing {@link Move}s after each
 * iteration to avoid expensive instantiation.
 *
 * @author Robert Harder
 * @see Move
 * @see Solution
 * @version 1.0
 * @since 1.0
 */

public interface MoveManager extends java.io.Serializable
{   
    
    /**
     * This method should return an array of all possible
     * moves to try at an iteration based on the passed
     * current solution. Note that the moves generated should
     * not depend on this exact solution object being the
     * one to be operated on. It may be a copy of the current
     * solution that is operated on.
     * 
     * @param solution The current solution.
     * @return All possible moves for this iteration from given solution
     * @see Solution
     * @see Move
     * @since 1.0
     */
    public abstract Move[] getAllMoves(Solution solution) throws Exception;


}   // end class MoveManager
    

