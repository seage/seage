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
package org.seage.problem.tsp.particles;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspParticle extends Particle
{
    /**
     * Represent order of cities
     */
    protected Integer[] _tour;

    /**
     * Array of cities
     */
    protected static City[] _cities;

    public TspParticle(City[] cities)
    {
        super( cities.length );
        _tour = new Integer[ cities.length ];
        _cities = cities;        
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
    public TspParticle clone()
    {
        TspParticle tspParticle = null;
        try
        {
            tspParticle = (TspParticle)super.clone();
            tspParticle.setTour( _tour.clone() );
            tspParticle.setCities( _cities.clone() );
        } catch (Exception ex)
        {
            Logger.getLogger(TspParticle.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tspParticle;
    }

}
