package org.seage.metaheuristic.tabusearch;

   

/**
 * The <tt>TabuList</tt> tracks which moves
 * are tabu and for how long.
 * 
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
 *
 * @author Robert Harder
 * @author Richard Malek
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


