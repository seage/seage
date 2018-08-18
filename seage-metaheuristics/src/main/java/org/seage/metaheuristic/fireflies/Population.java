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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Karel Durkota
 */
public class Population<S extends Solution>
{
    private ArrayList<S> _population;

    public Population()
    {
        _population = new ArrayList<S>();
    }

    public void addSolution(S solution)
    {
        _population.add(solution);
    }

    public S getSolution(int ix) throws Exception
    {
        try
        {
            return _population.get(ix);
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

    public List<S> getList()
    {
        return _population;
    }

    public void removeTwins() throws Exception
    {
        try
        {
            ArrayList<S> newPopulation = new ArrayList<S>();
            newPopulation.add(getSolution(0));
            for (int i = 1; i < _population.size(); i++)
            {
                S prev = _population.get(i - 1);
                S curr = _population.get(i);
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
        if (getSize() > newLength)
            _population = new ArrayList<S>(_population.subList(0, newLength));
    }

    @SuppressWarnings("unchecked")
	public S getBestSolution()
    {
        return (S)_population.get(0).clone();
    }

    public void mergePopulation(Population<S> population)
    {
        _population.addAll(population._population);
    }

    public List<S> getSolutions() throws Exception
    {
        return getSolutions(_population.size());
    }

	public List<S> getSolutions(int numSubjects) throws Exception
    {
        try
        {
            int length = _population.size();
            if (length > numSubjects)
                return _population.subList(0, numSubjects);
            else
                return _population;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
