/*******************************************************************************
 * Copyright (c) 2001 Robert Harder
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
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
 * This is the interface to implement when creating your own solution definitions.
 * It is usually easier to extend the {@link SolutionAdapter} class instead
 * which will handle these three required methods, including properly cloning
 * the array of objective values.
 * <p>
 * <em>It is essential that you still implement your own {@link #clone clone()}
 * method</em> and clone whatever custom properties you have in your solution definition.
 * </p>
 * <P>
 * For an excellent discussion of cloning techniques, see the
 * Java Developer Connection Tech Tip
 * <a href="http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html">
 * http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html</a>.
 * </P>
 * 
 * @author Robert Harder
 * @version 1.0
 * @since 1.0b
 */
public interface Solution 
extends java.lang.Cloneable, java.io.Serializable
{


    /**
     * If the value has been set for this solution, then the value will
     * be returned. Be careful if this returns null since this may
     * indicate that the {@link TabuSearch} has not yet
     * set the solution's value.
     * 
     * @return The value of the solution
     * @since 1.0
     */
    public abstract double[] getObjectiveValue();



    /**
     * Generally used by the {@link TabuSearch} to set the value of the
     * objective function
     * and set <tt>objectiveValid</tt> to <tt>true</tt>.
     * 
     * @param objValue The objective function value
     * @since 1.0
     */
    public abstract void setObjectiveValue( double[] objValue );

    
    /**
     * An essential Java method that returns of copy of the object. 
     * This should do whatever is necessary to ensure that the returned
     * {@link Solution} is identical to <tt>this</tt.
     * Be careful of only copying references to arrays and other objects
     * when a full copy is what may be needed.
     * <P>
     * For an excellent discussion of cloning techniques, see the
     * Java Developer Connection Tech Tip
     * <a href="http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html">
     * http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html</a>.
     * </P>
     * 
     * @return A copy of <tt>this</tt>.
     * @see java.lang.Cloneable
     * @since 1.0
     */
    public abstract Object clone();
        
}   // end SolutionBase class

