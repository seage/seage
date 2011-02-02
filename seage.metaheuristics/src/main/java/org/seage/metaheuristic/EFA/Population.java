/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     Karel Durkota
 */
package org.seage.metaheuristic.EFA;

import org.seage.metaheuristic.EFA.*;
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
