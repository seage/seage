package org.seage.metaheuristic.tabusearch;


/**
 * This exception is thrown when there is no current solution
 * Execution does not stop. The {@link TabuSearch} moves
 * on to the next iteration, giving other threads the opportunity
 * to set the current solution.
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
 * @author Robert Harder, rharder@usa.net
 * @author Richard Malek
 * @version 1.0
 * @since 1.0
 */


public class NoCurrentSolutionException extends java.lang.Exception
{

    /**
     * Constructs generic <tt>NoCurrentSolutionException</tt>.
     * This constructor only calls <tt>super()</tt>.
     *
     * @since 1.0
     */
    public NoCurrentSolutionException()
    {   super();
    }   // end constructor
    
    
    /**
     * Constructs a <tt>NoCurrentSolutionException</tt> with
     * the specified {@link java.lang.String}. This constructor calls
     * <tt>super( s )</tt>.
     *
     * @param s <code>String</code> describing the exception
     * @since 1.0
     */
    public NoCurrentSolutionException(String s)
    {   super( s );
    }   // end constructor
    
    
}   // end NoCurrentSolutionException

