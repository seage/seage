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
 *     Jan Zmatlik
 *     - Initial implementation
 */
package org.seage.metaheuristic.particles;

import java.util.Random;

/**
 *
 * @author Jan Zmatlik
 */
public class VelocityManager implements IVelocityManager
{
    Random _rnd = new Random();

    public void calculateNewVelocityAndPosition(Particle particle, Particle localMinimum, Particle globalMinimum,
            double alpha, double beta, double inertia)
    {
        double[] randomVector1 = new double[particle.getCoords().length];
        double[] randomVector2 = new double[particle.getCoords().length];

        for (int i = 0; i < particle.getCoords().length; i++)
        {
            randomVector1[i] = _rnd.nextDouble();
            randomVector2[i] = _rnd.nextDouble();
        }

        // w as inertial weight
        // about inertial weight - http://tracer.uc3m.es/tws/pso/basics.html
        // {*} as Hadamard Product
        // g as global minimum
        // l as local minimum
        // Vi as velocity vector of current particle
        // Xi as coords of current particle
        // T as iteration counter
        // Al as Alpha - Global acceleration constant
        // Be as Beta - Local acceleration constant
        // e1 as random vector
        // e2 as random vector
        //
        // V(T+1) = w*Vi(T) + Al*e1{*}[g - Xi(T)] + Be*e2[l - Xi(T)]
        for (int i = 0; i < particle.getCoords().length; i++)
        {
            particle.getVelocity()[i] = inertia * particle.getVelocity()[i] +
                    randomVector1[i] * alpha * (globalMinimum.getCoords()[i] - particle.getCoords()[i]) +
                    randomVector2[i] * beta * (localMinimum.getCoords()[i] - particle.getCoords()[i]);

            particle.getCoords()[i] += particle.getVelocity()[i];
        }
    }
}
