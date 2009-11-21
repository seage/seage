package org.seage.metaheuristic.tabusearch;


/**
 * The objective function is used to
 * evaluate a {@link Solution}. 
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
 * @rharder@usa.net
 * @version 1.0
 * @since 1.0
 */
public interface ObjectiveFunction extends java.io.Serializable
{

    
    
    /**
     * The {@link TabuSearch} expects an objective/
     * constraint penalties function to be able
     * to evaluate a solution's worth. The {@link TabuSearch} will
     * pass the <tt>ObjectiveFunction</tt> a {@link Solution} and
     * optionally pass a proposed {@link Move}. If the passed move is
     * not <tt>null</tt>, then the evaluation should consider the effect that executing
     * this move on the solution would have before evaluating it. If you can use some
     * sort of incremental evaluation technique, this will save you time. If you
     * must operate on the solution to actually calculate its value, you must
     * return the solution to its original state before the method returns.
     * <P>
     * Casting example:<BR>
     * <pre><code>
     *     public double[] evaluate( Solution soln, Move move )<BR>
     *     {
     *         MySolutionClass solution = (MySolutionClass) soln;
     *         // ...
     *
     *     }   // end evaluate
     * </code></pre>
     * <P>
     * The array of returned values will later be compared
     * lexicographically in the classic "goal-programming"
     * style. If instead you want some goals to overpower higher goals,
     * use the style of weighting the levels with appropriate values.
     * Although all numbers are stored and calculated as <tt>double</tt>s,
     * they are cast to <tt>float</tt>s before being compared.
     * <P>
     * Be careful that you do not "reuse" arrays which can lead to different
     * solutions sharing the same array of values in memory. A good technique
     * is to evaluate your values as scalars and then return a fresh array like this:
     * <P>
     * <pre><code>
     *     double val1 = ...;
     *     double val2 = ...;
     *     ...
     *     return new double[]{ val1, val2 };
     * </code></pre>
     *
     * 
     * @param soln The solution to evaluate
     * @param move If not <tt>null</tt> the proposed move
     * @return The function's value.
     * @see Solution
     * @see Move
     * @since 1.0
     */
    public abstract double[] evaluate( Solution soln, Move move ) throws Exception;
    
    
    
}   // end class ObjectiveFunction

