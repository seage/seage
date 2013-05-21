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
package org.seage.metaheuristic.genetics;

import java.util.*;

/**
 * @author Richard Malek (original)
 */
class Population<GeneType>
{
    private ArrayList<Subject<GeneType>> _population;

    public Population()
    {
        _population = new ArrayList<Subject<GeneType>>();
    }

    public void addSubject(Subject<GeneType> subject)
    {
        _population.add(subject);
    }

    public Subject<GeneType> getSubject(int ix) throws Exception
    {
        try
        {
            return (Subject<GeneType>)_population.get(ix);
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

    public List<Subject<GeneType>> getList()
    {
        return _population;
    }

    public void removeTwins() throws Exception
    {
        try
        {
            ArrayList<Subject<GeneType>> newPopulation = new ArrayList<Subject<GeneType>>();
            newPopulation.add(getSubject(0));
            for (int i = 1; i < _population.size(); i++)
            {
                Subject<GeneType> prev = (Subject<GeneType>)_population.get(i - 1);
                Subject<GeneType> curr = (Subject<GeneType>)_population.get(i);
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
            _population = new ArrayList<Subject<GeneType>>(_population.subList(0, newLength));
    }

    public Subject<GeneType> getBestSubject()
    {
        return _population.get(0);
    }

    public void mergePopulation(Population<GeneType> population)
    {
        _population.addAll(population._population);
    }

    public List<Subject<GeneType>> getSubjects() throws Exception
    {
        return getSubjects(_population.size());
    }

    public List<Subject<GeneType>> getSubjects(int numSubjects) throws Exception
    {
        try
        {
            int length = _population.size();
            if (length > numSubjects)
                return _population.subList(0,numSubjects);
            else
                return _population;
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }

	public void sort(SubjectComparator<GeneType> subjectComparator)
	{
		Collections.sort(_population, subjectComparator);		
	}
}
