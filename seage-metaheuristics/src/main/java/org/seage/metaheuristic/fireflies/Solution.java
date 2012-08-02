/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Robert Harder
 *     - Initial implementation
 */

package org.seage.metaheuristic.fireflies;


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
