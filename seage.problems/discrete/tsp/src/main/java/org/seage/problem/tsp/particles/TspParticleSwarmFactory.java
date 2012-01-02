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
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.tsp.particles;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.particles.ParticleSwarmAdapter;


import org.seage.aal.data.ProblemConfig;

import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;


/**
 *
 * @author Jan Zmatlik
 */
@Annotations.AlgorithmId("ParticleSwarm")
@Annotations.AlgorithmName("Particle Swarm")
public class TspParticleSwarmFactory implements IAlgorithmFactory
{
    private int _numParticles;

    private TspObjectiveFunction _objectiveFunction;


    public Class getAlgorithmClass() {
        return ParticleSwarmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, ProblemConfig config) throws Exception
    {
        IAlgorithmAdapter algorithm;

        final City[] cities = ((TspProblemInstance)instance).getCities();

        _objectiveFunction = new TspObjectiveFunction(cities);

        algorithm = new ParticleSwarmAdapter(
                generateInitialSolutions(cities),
                _objectiveFunction,
                false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _numParticles = source.length;
                _initialParticles = generateInitialSolutions(cities);
                for(int i = 0; i < source.length; i++)
                {
                    Integer[] tour = ((TspParticle)_initialParticles[i]).getTour();
                    for(int j = 0; j < source[i].length; j++)
                    {
                        tour[j] = (Integer)source[i][j];
                    }
                }
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                int numOfParticles = _particleSwarm.getParticles().length;
                Object[][] source = new Object[ numOfParticles ][ cities.length ];

                for(int i = 0; i < source.length; i++)
                {
                    source[i] = new Integer[ cities.length ];
                    Integer[] tour = ((TspParticle)_particleSwarm.getParticles()[i]).getTour();
                    for(int j = 0; j < source[i].length; j++)
                    {
                        source[i][j] = tour[j];
                    }
                }

                // TODO: A - need to sort source by fitness function of each tour
                
                return source;
            }
        };
        
        return algorithm;
    }

    private Particle[] generateInitialSolutions(City[] cities) throws Exception
    {
        Particle[] particles = generateTspRandomParticles( _numParticles, cities );

        for(Particle particle : particles)
        {
            // Initial coords
            for(int i = 0; i < cities.length; i++)
                particle.getCoords()[i] = Math.random();

            // Initial velocity
            for(int i = 0; i < cities.length; i++)
                particle.getVelocity()[i] = Math.random();

            // Evaluate
            _objectiveFunction.setObjectiveValue( particle );
        }

        return particles;
    }

    void printArray(Integer[] array)
    {
        for(int i = 0; i< array.length; i++)
            System.out.print(" " + array[i]);
    }

    private Particle[] generateTspRandomParticles(int count, City[] cities)
    {
        TspRandomParticle[] particles = new TspRandomParticle[count];
        for(int i = 0; i < count; i++)
            particles[i] = new TspRandomParticle( cities.length );

        return particles;
    }
}
