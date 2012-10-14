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

public interface MoveManager
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
    

