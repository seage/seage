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
package org.seage.problem.qap.tabusearch;

import org.seage.metaheuristic.tabusearch.SolutionAdapter;

/**
 *
 * @author Karel Durkota
 */
public class QapSolution extends SolutionAdapter
{
    protected Integer[] _assign;

    public QapSolution()
    {
    } // Appease clone()

    public QapSolution(Double[][][] customers)
    {
        // Crudely initialize solution
        _assign = new Integer[customers.length];
        for (int i = 0; i < customers.length; i++)
            _assign[i] = i;
    } // end constructor

    public QapSolution(Integer[] assign)
    {
        _assign = assign;
    }

    @Override
    public Object clone()
    {
        QapSolution copy = (QapSolution) super.clone();
        copy._assign = this._assign.clone();
        return copy;
    } // end clone

    public Integer[] getAssign()
    {
        return _assign;
    }

    public void setAssign(Integer[] assign)
    {
        _assign = assign;
    }

    @Override
    public String toString()
    {
        StringBuffer s = new StringBuffer();

        s.append("[");
        for (int i = _assign.length - 1; i >= 1; i--)
        {
            s.append((_assign[i] + 1));
            s.append(",");
        }
        s.append((_assign[_assign[0]] + 1) + "]");

        return s.toString();
    } // end toString

} // end class MySolution
