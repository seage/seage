/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.problem.tsp.sannealing;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspSolution extends Solution
{
    /**
     * Represent order of cities
     */
    private Integer[] _tour;

    /**
     * Array of cities
     */
    private City[] _cities;

    public TspSolution(City[] cities)
    {
        _tour = new Integer[cities.length];
        _cities = cities;

        initTourOrder();
    }

    /**
     * Sort and fill the tour
     */
    private void initTourOrder()
    {
        int tourLength = _tour.length;
        for(int i = 0; i < tourLength; i++) _tour[i] = i;
    }
    
    public Integer[] getTour()
    {
        return _tour;
    }

     public void setTour(Integer[] tour)
    {
        _tour = tour;
    }

    public City[] getCities()
    {
        return _cities;
    }
    
    public void setCities(City[] cities)
    {
        _cities = cities;
    }

    @Override
    public TspSolution clone()
    {
        TspSolution tspSolution = null;
        try
        {
            tspSolution = (TspSolution)super.clone();
            tspSolution.setTour( this._tour.clone() );
            tspSolution.setCities( this._cities.clone() );
        } catch (CloneNotSupportedException ex)
        {
            Logger.getLogger(TspSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tspSolution;
    }

}
