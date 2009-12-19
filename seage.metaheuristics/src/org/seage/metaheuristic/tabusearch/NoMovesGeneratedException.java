package org.seage.metaheuristic.tabusearch;


/**
 * This exception is thrown when {@link MoveManager#getAllMoves}
 * (in the {@link MoveManager}returns no moves.
 * Execution does not stop. The {@link TabuSearch} moves
 * on to the next iteration and again requests
 * {@link MoveManager#getAllMoves} from the {@link MoveManager}.
 * 
 *
 *
 *<p><em>This code is licensed for public use under the Common Public License version 0.5.</em><br/>
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
 * @see MoveManager
 * @see TabuSearch
 * @version 1.0
 * @since 1.0
 */
public class NoMovesGeneratedException extends java.lang.Exception
{
    
    /**
     * Constructs generic <tt>NoMovesGeneratedException</tt>.
     * This constructor only calls <code>super()</code> and quits.
     *
     * @since 1.0
     */
    public NoMovesGeneratedException()
    {   super();
    }   // end constructor
    
    
    /**
     * Constructs a <tt>NoMovesGeneratedException</tt> with
     * the specified {@link java.lang.String}. This constructor calls
     * <code>super( s )</code> and quits.
     *
     * @param s {@link String} describing the exception
     * @since 1.0
     **/
    public NoMovesGeneratedException( String s )
    {   super( s );
    }   // end constructor
    
    
    
}   // end NoMovesGeneratedException

