package org.seage.metaheuristic.genetics;

import java.util.*;

/**
 * Summary description for Population.
 */
public class Population
{
    private ArrayList _population;

    public Population()
    {
        _population = new ArrayList();
    }

    public void addSubject(Subject subject)
    {
        _population.add(subject);
    }

    public Subject getSubject(int ix) throws Exception
    {
        try
        {
            return (Subject)_population.get(ix);
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
            newPopulation.add(getSubject(0));
            for (int i = 1; i < _population.size(); i++)
            {
                Subject prev = (Subject)_population.get(i - 1);
                Subject curr = (Subject)_population.get(i);
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

    public Subject getBestSubject()
    {
        return (Subject)_population.get(0);
    }

    public void mergePopulation(Population population)
    {
        _population.addAll(population._population);
    }

    public Subject[] getSubjects() throws Exception
    {
        return getSubjects(_population.size());
    }

    public Subject[] getSubjects(int numSubjects) throws Exception
    {
        try
        {
            int length = _population.size();
            if (length > numSubjects)
                return (Subject[])_population.subList(0,numSubjects).toArray(new Subject[0]);
            else
                return (Subject[])_population.toArray(new Subject[0]);
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
