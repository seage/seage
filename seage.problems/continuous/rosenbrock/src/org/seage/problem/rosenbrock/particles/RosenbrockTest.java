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
        int dimension = 2;

//        Particle[] particles =
//        {
//            new Particle( dimension ) ,
//            new Particle( dimension ) ,
//            new Particle( dimension ) ,
//            new Particle( dimension )
//        };

        RosenbrockObjectiveFunction objFunction = new RosenbrockObjectiveFunction();

        Particle[] particles = generateParticles( 5 , 2 );

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

        ParticleSwarm pso = new ParticleSwarm( objFunction , new RosenbrockMoveManager() );
        pso.setMaximalIterationCount( 1000 );
        pso.setMaximalVelocity( 1.0 );
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
