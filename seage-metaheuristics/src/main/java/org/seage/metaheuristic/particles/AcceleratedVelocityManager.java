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
