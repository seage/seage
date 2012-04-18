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
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.metaheuristic.sannealing;

import java.io.Serializable;

/**
 *
 * @author Jan Zmatlik
 */
public class Solution implements Cloneable, Serializable {

    /**
     * The value is value of Solution
     */
    private double _value = Double.MAX_VALUE;

    /**
     * Returns the value of Solution
     * @return double value
     */
    public double getObjectiveValue()
    {
        return _value;
    }

    /**
     * Sets the value of Solution
     * @param double objValue
     */
    public void setObjectiveValue(double objValue)
    {
        _value = objValue;
    }

    @Override
    public  Solution clone()
    {
        try
        {
            Solution sol = (Solution)super.clone();
            sol._value = _value;
            return sol;
        }
        catch(CloneNotSupportedException e)
        {
            throw new InternalError( e.toString() );
        }
    }
}
