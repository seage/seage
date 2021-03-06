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

import org.seage.metaheuristic.tabusearch.SolutionAdapter;

/**
 * Summary description for JsspSolution.
 */
public class JsspSolution extends SolutionAdapter
{
    private Integer[] _jobArray;

    public JsspSolution(Integer[] jobArray)
    {
        _jobArray = jobArray.clone();
    }

    public JsspSolution(int numJobs, int numOpers)
    {
        _jobArray= new Integer[numJobs*numOpers];

        int i=0;
        for(int j=0;j<numJobs;j++)
            for(int k=0;k<numOpers;k++)
                _jobArray[i++] = j+1;
        
    }

    public Integer[] getJobArray()
    {
        return _jobArray;
    }

    @Override
    public String toString()
    {
        return getObjectiveValue()[0] + "";
    }

    @Override
    public Object clone()
    {
        JsspSolution copy = (JsspSolution) super.clone();
        copy._jobArray = this._jobArray.clone();
        return copy;
    } // end clone
}
