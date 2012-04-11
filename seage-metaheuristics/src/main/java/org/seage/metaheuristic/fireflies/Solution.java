/*
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
 */

package org.seage.metaheuristic.fireflies;

import org.seage.metaheuristic.fireflies.*;

/**
 *
 * @author Karel Durkota
 */
public interface Solution extends java.lang.Cloneable, java.io.Serializable{

    /**
     * If the value has been set for this solution, then the value will
     * be returned. Be careful if this returns null since this may
     * indicate that the {@link FireflySearch} has not yet
     * set the solution's value.
     *
     * @return The value of the solution
     * @since 1.0
     */
    public abstract double[] getObjectiveValue();

    /**
     * Generally used by the {@link FireflySearch} to set the value of the
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
    public abstract boolean equals(Solution s);
}
