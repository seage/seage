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

import org.seage.metaheuristic.particles.IObjectiveFunction;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TourProvider;

/**
 *
 * @author Jan Zmatlik
 */
public class TspObjectiveFunction implements IObjectiveFunction
{
    private City[] _cities;

    public TspObjectiveFunction(City[] cities)
    {
        _cities = cities;
    }

    public void setObjectiveValue(Particle particle)
    {
        TspParticle currentParticle = ((TspParticle)particle);
        double distance = 0.0;
        Integer[] tour = currentParticle.getTour();

        try
        {
            distance = (int)TourProvider.getTourLenght(tour, _cities);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
 
        particle.setEvaluation( distance );
    }
}

