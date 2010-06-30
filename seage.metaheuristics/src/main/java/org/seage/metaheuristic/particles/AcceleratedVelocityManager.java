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

package org.seage.metaheuristic.particles;

import java.util.Random;

/**
 *
 * @author Jan Zmátlík
 */
public class AcceleratedVelocityManager implements IVelocityManager {

    Random _rnd = new Random();
    public void calculateNewVelocityAndPosition(Particle particle, Particle localMinimum, Particle globalMinimum, double alpha, double beta, double inertia)
    {
        for(int i = 0; i < particle.getCoords().length; i++)
        {
            particle.getVelocity()[i] = particle.getVelocity()[i]
                    + alpha * (_rnd.nextDouble() - 0.5d)
                    + beta * (globalMinimum.getCoords()[i] - particle.getCoords()[i]);
            
            particle.getCoords()[i] += particle.getVelocity()[i];
        }
    }
}
