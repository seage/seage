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
 *     Karel Durkota
 */
package org.seage.metaheuristic.fireflies;

import org.seage.metaheuristic.fireflies.*;
import java.util.*;

/**
 * @author Karel Durkota
 */
public class Population
{
    private ArrayList _population;

    public Population()
    {
        _population = new ArrayList();
    }

    public void addSolution(Solution solution)
    {
        _population.add(solution);
    }

    public Solution getSolution(int ix) throws Exception
    {
        try
        {
            return (Solution)_population.get(ix);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public void removeAll()
    {
        _population.clear();
    }

    public int getSize()
    {
        return _population.size();
    }

    public List getList()
    {
        return _population;
    }

    public void removeTwins() throws Exception
    {
        try
        {
            ArrayList newPopulation = new ArrayList();
            newPopulation.add(getSolution(0));
            for (int i = 1; i < _population.size(); i++)
            {
                Solution prev = (Solution)_population.get(i - 1);
                Solution curr = (Solution)_population.get(i);
                if (curr.hashCode() != prev.hashCode())
                {
                    newPopulation.add(curr);
                }
            }
            _population.clear();
            _population.addAll(newPopulation);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

    public void resize(int newLength)
    {
        if(getSize() > newLength)
            _population = new ArrayList(_population.subList(0, newLength));
    }

    public Solution getBestSolution()
    {
        return (Solution)_population.get(0);
    }

    public void mergePopulation(Population population)
    {
        _population.addAll(population._population);
    }

    public Solution[] getSolutions() throws Exception
    {
        return getSolutions(_population.size());
    }

    public Solution[] getSolutions(int numSubjects) throws Exception
    {
        try
        {
            int length = _population.size();
            if (length > numSubjects)
                return (Solution[])_population.subList(0,numSubjects).toArray(new Solution[0]);
            else
                return (Solution[])_population.toArray(new Solution[0]);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
