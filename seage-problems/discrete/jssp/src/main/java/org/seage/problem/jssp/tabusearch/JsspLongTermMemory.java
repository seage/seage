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

import org.seage.metaheuristic.tabusearch.LongTermMemory;
import org.seage.metaheuristic.tabusearch.Solution;

/**
 * Summary description for JsspLongTermMemory.
 */
public class JsspLongTermMemory implements LongTermMemory
{
    public JsspLongTermMemory()
    {
        //
        // TODO: Add Constructor Logic here
        //
    }

    @Override
    public void clearMemory()
    {
    }

    @Override
    public void memorizeSolution(Solution soln, boolean newBestSoln)
    {
        //if (newBestSoln)
        //    System.out.println(soln);
    }

    @Override
    public Solution diversifySolution()
    {
        return null;
    }

    @Override
    public void resetIterNumber()
    {
    }
}
