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
public interface Move extends java.io.Serializable
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

