/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Martin Zaloga - Initial implementation
 */
package org.seage.problem.sat.grasp;

import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class SatSolution extends Solution
{

    protected static final long serialVersionUID = -7791570406526861971L;
    protected Boolean[] _litValues;
    // protected Random _rnd;

    public SatSolution()
    {
        // _rnd = new Random();
    }

    public void initLiterals(int size)
    {
        _litValues = new Boolean[size];
        for (int i = 0; i < size; i++)
        {
            _litValues[i] = true;
        }
    }

    public int getLiteralCount()
    {
        return _litValues.length;
    }

    public Boolean[] getLiteralValues()
    {
        return _litValues;
    }

    public void setLiteralValues(Boolean[] litValues)
    {
        _litValues = litValues;
    }

    public boolean getLiteralValue(int index)
    {
        return _litValues[index];
    }

    public void setLiteralValue(int index, Boolean value)
    {
        _litValues[index] = value;
    }

    public void negLiteral(int index)
    {
        if (_litValues[index])
        {
            _litValues[index] = false;
        }
        else
        {
            _litValues[index] = true;
        }
    }

    public void printLiterals()
    {
        for (int i = 0; i < _litValues.length; i++)
        {
            System.out.println("literal[" + i + "] = " + _litValues[i]);
        }
    }
}
