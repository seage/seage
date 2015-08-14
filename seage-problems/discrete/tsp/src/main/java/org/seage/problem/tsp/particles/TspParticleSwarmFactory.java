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
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.tsp.particles;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.particles.ParticleSwarmAdapter;
import org.seage.aal.problem.ProblemInstance;
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

    @Override
    public Class<ParticleSwarmAdapter> getAlgorithmClass()
    {
        return ParticleSwarmAdapter.class;
    }

    @Override
    public IAlgorithmAdapter createAlgorithm(ProblemInstance instance) throws Exception
    {
        IAlgorithmAdapter algorithm;

        final City[] cities = ((TspProblemInstance) instance).getCities();

        _objectiveFunction = new TspObjectiveFunction(cities);

        algorithm = new ParticleSwarmAdapter(
                generateInitialSolutions(cities),
                _objectiveFunction,
                false, "")
        {
            @Override
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _numParticles = source.length;
                _initialParticles = generateInitialSolutions(cities);
                for (int i = 0; i < source.length; i++)
                {
                    Integer[] tour = ((TspParticle) _initialParticles[i]).getTour();
                    for (int j = 0; j < source[i].length; j++)
                    {
                        tour[j] = (Integer) source[i][j];
                    }
                }
            }

            @Override
            public Object[][] solutionsToPhenotype() throws Exception
            {
                int numOfParticles = _particleSwarm.getParticles().length;
                Object[][] source = new Object[numOfParticles][cities.length];

                for (int i = 0; i < source.length; i++)
                {
                    source[i] = new Integer[cities.length];
                    Integer[] tour = ((TspParticle) _particleSwarm.getParticles()[i]).getTour();
                    for (int j = 0; j < source[i].length; j++)
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
        Particle[] particles = generateTspRandomParticles(_numParticles, cities);

        for (Particle particle : particles)
        {
            // Initial coords
            for (int i = 0; i < cities.length; i++)
                particle.getCoords()[i] = Math.random();

            // Initial velocity
            for (int i = 0; i < cities.length; i++)
                particle.getVelocity()[i] = Math.random();

            // Evaluate
            _objectiveFunction.setObjectiveValue(particle);
        }

        return particles;
    }

    void printArray(Integer[] array)
    {
        for (int i = 0; i < array.length; i++)
            System.out.print(" " + array[i]);
    }

    private Particle[] generateTspRandomParticles(int count, City[] cities)
    {
        TspRandomParticle[] particles = new TspRandomParticle[count];
        for (int i = 0; i < count; i++)
            particles[i] = new TspRandomParticle(cities.length);

        return particles;
    }
}
