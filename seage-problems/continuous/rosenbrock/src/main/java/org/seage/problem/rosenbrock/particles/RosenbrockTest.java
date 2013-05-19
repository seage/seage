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
package org.seage.problem.rosenbrock.particles;

import org.seage.metaheuristic.particles.Particle;
import org.seage.metaheuristic.particles.ParticleSwarm;

/**
 *
 * @author Jan Zmatlik
 */
public class RosenbrockTest
{
    public static void main(String[] args)
    {
        int dimension = 80;
        int numberOfParticles = 100;
        //int maximalVelocity = 1.0d;

        RosenbrockObjectiveFunction objFunction = new RosenbrockObjectiveFunction();

        Particle[] particles = generateParticles( numberOfParticles , dimension );

        for(Particle particle : particles)
        {
            // Initial coords
            for(int i = 0; i < dimension; i++)
                particle.getCoords()[i] = Math.random();

            // Initial velocity
            for(int i = 0; i < dimension; i++)
                particle.getVelocity()[i] = Math.random();

            // Evaluate
            objFunction.setObjectiveValue( particle );
        }

        ParticleSwarm pso = new ParticleSwarm( objFunction );
        pso.setMaximalIterationCount( 1000 );
        pso.setMaximalVelocity( 0.9 );
        pso.setMinimalVelocity( -0.9 );
        pso.setInertia( 0.9 );
        pso.setAlpha(0.2);
        pso.setBeta(0.5);
        pso.startSearching( particles );
    }

    private static Particle[] generateParticles(int count, int dimension)
    {
        Particle[] particles = new Particle[count];
        for(int i = 0; i < count; i++)
            particles[i] = new Particle( dimension );

        return particles;
    }
}
