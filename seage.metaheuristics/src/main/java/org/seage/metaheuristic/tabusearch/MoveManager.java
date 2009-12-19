package org.seage.metaheuristic.tabusearch;


/**
 * The <tt>MoveManager</tt> determines which moves are available
 * at any given time (or given solution). As a performance boost, you
 * may want to consider reusing {@link Move}s after each
 * iteration to avoid expensive instantiation.
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
 *
 * @author Robert Harder
 * @author Richard Malek
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
    

