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
 * There is a great deal of flexibility
 * in defining these moves. A move could be toggling
 * a binary variable on or off. It could be swapping
 * two variables in a sequence. The important thing
 * is that it affects the solution in some way.
 * 
 * @author Robert Harder
 * @see ObjectiveFunction
 * @see Solution
 * @version 1.0
 * @since 1.0
 */        
public interface Move
{   

    /**
     * The required method <tt>operateOn</tt> accepts
     * a solution to modify and does so. Note that a new
     * solution is not returned. The original solution
     * is modified. 
     * 
     * @param soln The solution to be modified.
     * @see Solution
     * @since 1.0
     */
    public abstract void operateOn( Solution soln );

                   
}   // end class Move

