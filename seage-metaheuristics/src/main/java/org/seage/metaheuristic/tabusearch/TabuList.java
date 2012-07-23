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


