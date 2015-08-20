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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.metaheuristic.grasp;

/**
 *
 * @author Martin Zaloga
 */
public class Solution implements java.lang.Cloneable, java.io.Serializable
{
    private static final long serialVersionUID = -5024798712835848962L;
    /**
     * The _value is value of Solution
     */
    double _value = Double.MAX_VALUE;

    /**
     * Returns the value of Solution
     * @return - The rating of Solution
     */
    public double getObjectiveValue()
    {
        return _value;
    }

    /**
     * Sets the value of Solution
     * @param objValue - Evaluation of the solution
     */
    public void setObjectiveValue(double objValue)
    {
        _value = objValue;
    }
}
