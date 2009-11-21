package org.seage.metaheuristic.tabusearch;


/**
 * There is a great deal of flexibility
 * in defining these moves. A move could be toggling
 * a binary variable on or off. It could be swapping
 * two variables in a sequence. The important thing
 * is that it affects the solution in some way.
 * 
 *
 * <p><em>This code is licensed for public use under the Common Public License version 0.5.</em><br/>
 * The Common Public License, developed by IBM and modeled after their industry-friendly IBM Public License,
 * differs from other common open source licenses in several important ways:
 * <ul>
 *  <li>You may include this software with other software that uses a different (even non-open source) license.</li>
 *  <li>You may use this software to make for-profit software.</li>
 *  <li>Your patent rights, should you generate patents, are protected.</li>
 * </ul>
 * </p>
 * <p><em>Copyright � 2001 Robert Harder</em></p>
 *
 * @author Robert Harder
 * @author Richard Malek
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

