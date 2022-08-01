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
 *     Karel Durkota
 */

package org.seage.metaheuristic.fireflies;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * This is the class to extend when creating your own solution definitions.
 * <p>
 * <em>It is essential that you still implement your own {@link #clone clone()}
 * method</em> and clone whatever custom properties you have in your solution
 * definition.
 * </p>
 * <p>
 * For an excellent discussion of cloning techniques, see the Java Developer
 * Connection Tech Tip <a href=
 * "http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html">
 * http://developer.java.sun.com/developer/JDCTechTips/2001/tt0306.html</a>.
 * </p>
 * 
 * <p>
 * Here is an example of how to clone your solution. Notice that it properly
 * calls the <code>super.clone()</code> method so that the
 * {@link SolutionAdapter}'s {@link SolutionAdapter#clone clone()} method is
 * also called. Then an <code>int</code> array is cloned and a
 * {@link java.util.HashMap} is cloned.
 * 
 * <code>
 *     ...
 *     public Object clone()
 *     {
 *         MySolution sol = (MySolution) super.clone();
 *         sol.myIntArray = (int[])      this.myIntArray.clone();
 *         sol.myMap      = (HashMap)    this.myMap.clone();
 *     }   // end clone
 *     ...
 * </code>
 * </p>
 *
 * @author Robert Harder
 * @version 1.0b
 * @since 1.0b
 */
public abstract class SolutionAdapter implements Solution {

  /** Objective function value. */
  private double[] objectiveValue;

  @Override
  public abstract boolean equals(Object s);

  @Override
  public abstract int hashCode();

  /**
   * If the value has been set for this solution, then the value will be returned.
   * Be careful if this returns null since this may indicate that the
   * {@link FireflySearch} has not yet set the solution's value.
   * 
   * @return The value of the solution
   * @since 1.0
   */
  @Override
  public final double[] getObjectiveValue() {
    return this.objectiveValue;
  } // end getValue

  /**
   * Generally used by the {@link FireflySearch} to set the value of the objective
   * function and set <code>objectiveValid</code> to <code>true</code>.
   * 
   * @param objValue The objective function value
   * @since 1.0
   */

  @Override
  public final void setObjectiveValue(double[] objValue) {
    this.objectiveValue = objValue;
  } // end setObjectiveValue

  /**
   * An essential Java method that returns of copy of the object. Specifically the
   * <code>double</code> array that holds the objective value is properly cloned.
   * 
   * @return A copy of <code>this</code>.
   * @see java.lang.Cloneable
   * @since 1.0
   */
  @Override
  public Object clone() throws CloneNotSupportedException {
    try {
      Solution sol = (Solution) super.clone(); // Java's default cloning

      double[] copyThisObjVal = // Get our objective value
          getObjectiveValue();

      if (copyThisObjVal != null) // Make sure the array isn't null
      {
        this.objectiveValue = // Clone the array using the built-in clone method
            copyThisObjVal.clone();

        /*
         * // The primitive boolean field objectiveValid nothing to do here // is
         * automatically cloned by the
         */ // java.lang.Object's clone method.
      } // end if: not null

      return sol;
    } catch (java.lang.CloneNotSupportedException e) {
      throw new InternalError(e.toString()); // Throw a runtime error
    } // end catch
  } // end clone

  @Override
  public String toString() {
    String result = "";

    double[] obj = getObjectiveValue();
    if (obj == null) {
      return "null";
    }
    NumberFormat formatter = new DecimalFormat("0.000", new DecimalFormatSymbols(Locale.US));
    for (int i = 0; i < obj.length; i++) {
      result += formatter.format(obj[i]) + "\t";
    }

    result += hashCode();
    return result;
  }

} // end SolutionBase class
