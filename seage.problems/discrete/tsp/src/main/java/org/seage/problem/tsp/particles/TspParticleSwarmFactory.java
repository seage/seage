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

import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.particles.ParticleSwarmAdapter;
import org.seage.data.DataNode;

import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.tsp.City;


/**
 *
 * @author Jan Zmatlik
 */
public class TspParticleSwarmFactory implements IAlgorithmFactory
{
    private City[] _cities;

    private int _numParticles;

    private TspObjectiveFunction _objectiveFunction;

    public TspParticleSwarmFactory(DataNode params, City[] cities) throws Exception
    {
        _cities = cities;
        _numParticles = params.getValueInt("numSolutions");
    }

    public IAlgorithmAdapter createAlgorithm(DataNode algorithmParams) throws Exception
    {
        IAlgorithmAdapter algorithm;
        _objectiveFunction = new TspObjectiveFunction(_cities);

        algorithm = new ParticleSwarmAdapter(
                generateInitialSolutions(),
                _objectiveFunction,
                false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
//                Particle[] particles = generateInitialSolutions();
//                for(int i = 0; i < source.length; i++)
//                {
//                    Integer[] tour = ((TspParticle)particles[i]).getTour();
//                    for(int j = 0; j < source[i].length; j++)
//                    {
//                        tour[j] = (Integer)source[i][j];
//                    }
//                }
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                int numOfParticles = _particleSwarm.getParticles().length;
                Object[][] source = new Object[ numOfParticles ][ _cities.length ];

                for(int i = 0; i < source.length; i++)
                {
                    source[i] = new Integer[ _cities.length ];
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

    private Particle[] generateInitialSolutions() throws Exception
    {
        Particle[] particles = generateTspRandomParticles( _numParticles );

        for(Particle particle : particles)
        {
            // Initial coords
            for(int i = 0; i < _cities.length; i++)
                particle.getCoords()[i] = Math.random();

            // Initial velocity
            for(int i = 0; i < _cities.length; i++)
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

    private Particle[] generateTspRandomParticles(int count)
    {
        TspRandomParticle[] particles = new TspRandomParticle[count];
        for(int i = 0; i < count; i++)
            particles[i] = new TspRandomParticle( _cities.length );

        return particles;
    }
}
