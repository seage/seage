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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
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
 * This is the interface to implement when creating your own solution
 * definitions. It is usually easier to extend the {@link SolutionAdapter} class
 * instead which will handle these three required methods, including properly
 * cloning the array of objective values.
 * <p>
 * <em>It is essential that you still implement your own {@link #clone clone()}
 * method</em> and clone whatever custom properties you have in your solution
 * definition.
 * </p>
 * <P>
 * For an excellent discussion of cloning techniques, see the Java Developer
 * Connection Tech Tip <a href=
 * "http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html">
 * http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html</a>.
 * </P>
 * 
 * @author Robert Harder
 * @version 1.0
 * @since 1.0b
 */
public interface Solution extends Cloneable {

  /**
   * If the value has been set for this solution, then the value will be returned.
   * Be careful if this returns null since this may indicate that the
   * {@link TabuSearch} has not yet set the solution's value.
   * 
   * @return The value of the solution
   * @since 1.0
   */
  public abstract double[] getObjectiveValue();

  /**
   * Generally used by the {@link TabuSearch} to set the value of the objective
   * function and set <code>objectiveValid</code> to <code>true</code>.
   * 
   * @param objValue The objective function value
   * @since 1.0
   */
  public abstract void setObjectiveValue(double[] objValue);

  /**
   * An essential Java method that returns of copy of the object. This should do
   * whatever is necessary to ensure that the returned {@link Solution} is
   * identical to <code>this</code>.
   * Be careful of only copying references to arrays and other objects
   * when a full copy is what may be needed.
   * <P>
   * For an excellent discussion of cloning techniques, see the
   * Java Developer Connection Tech Tip
   * <a href=
  "http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html">
   * http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html</a>.
   * </P>
   * 
   * &#64;return A copy of <code>this</code>.
   * 
   * @see java.lang.Cloneable
   * @since 1.0
   */
  public abstract Object clone();

} // end SolutionBase class
