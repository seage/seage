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
package org.seage.problem.jssp.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 * Summary description for JsspMove.
 */
public class JsspMove implements Move
{
    private int _ix1;
    private int _ix2;

    public JsspMove(int ix1, int ix2)
    {
        _ix1 = ix1;
        _ix2 = ix2;
    }

    @Override
    public void operateOn(Solution soln)
    {
        JsspSolution solution = (JsspSolution) soln;
        int tmp = solution.getJobArray()[_ix1];
        solution.getJobArray()[_ix1] = solution.getJobArray()[_ix2];
        solution.getJobArray()[_ix2] = tmp;
    }

    public int getIndex1()
    {
        return _ix1;
    }

    public int getIndex2()
    {
        return _ix2;
    }

    @Override
    public int hashCode()
    {
        return (_ix1 << 10) + _ix2 << 5;
    }
}
