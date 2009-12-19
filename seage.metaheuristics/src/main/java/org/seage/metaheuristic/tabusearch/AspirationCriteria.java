package org.seage.metaheuristic.tabusearch;

/**
 * The optional aspiration criteria allows you to override a move's tabu status
 * if it offers something good such as resulting in a new best solution.
 * A <tt>null</tt> value implies no aspiration criteria.
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
 * @version 1.0
 * @since 1.0
 */
        
public interface AspirationCriteria extends java.io.Serializable
{   

    /**
     * Determine if the proposed tabu move should in fact be allowed
     * because of some aspiration criteria such as being better than
     * the previously-known best solution. 
     * The proposed move, the solution it would operate on, and
     * the resulting objective function value are passed along with the
     * {@link TabuSearch} itself in case there's some other data you want.
     *
     * @param soln The solution to be modified
     * @param move The proposed move
     * @param value The resulting objective function value
     * @param tabuSearch The {@link TabuSearch} controlling the transaction
     * @see Solution
     * @see Move
     * @see TabuSearch
     * @since 1.0
     */
    public abstract boolean overrideTabu( 
        Solution baseSolution, 
        Move proposedMove, 
        double[] proposedValue, 
        ITabuSearch tabuSearch );

                   
}   // end class AspirationCritera

