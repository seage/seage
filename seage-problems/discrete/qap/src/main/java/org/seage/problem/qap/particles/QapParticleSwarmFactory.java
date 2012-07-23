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
 *     Karel Durkota
 *     - Initial implementation
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.qap.particles;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.data.ProblemInstanceInfo;
import org.seage.aal.algorithm.particles.ParticleSwarmAdapter;
import org.seage.aal.data.ProblemConfig;
import org.seage.data.DataNode;

import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.qap.QapProblemInstance;


/**
 *
 * @author Karel Durkota
 */
@Annotations.AlgorithmId("ParticleSwarm")
@Annotations.AlgorithmName("Particle Swarm")
public class QapParticleSwarmFactory implements IAlgorithmFactory
{
    //private Double[][][] _facilityLocation;

    private int _numParticles;

    private QapObjectiveFunction _objectiveFunction;

    public Class getAlgorithmClass() {
        return ParticleSwarmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(ProblemInstanceInfo instance, ProblemConfig config) throws Exception
    {
            throw new UnsupportedOperationException();
    }
    
    public IAlgorithmAdapter createAlgorithm2(ProblemInstanceInfo instance, DataNode config) throws Exception
    {    
        IAlgorithmAdapter algorithm;
        final Double[][][] facilityLocation = ((QapProblemInstance)instance).getFacilityLocation();
        
        _objectiveFunction = new QapObjectiveFunction(facilityLocation);

        algorithm = new ParticleSwarmAdapter(
                generateInitialSolutions(facilityLocation.length),
                _objectiveFunction,
                false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _numParticles = source.length;
                _initialParticles = generateInitialSolutions(facilityLocation.length);
                for(int i = 0; i < source.length; i++)
                {
                    Integer[] tour = ((QapParticle)_initialParticles[i]).getAssign();
                    for(int j = 0; j < source[i].length; j++)
                    {
                        tour[j] = (Integer)source[i][j];
                    }
                }
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                int numOfParticles = _particleSwarm.getParticles().length;
                Object[][] source = new Object[ numOfParticles ][ facilityLocation.length ];

                for(int i = 0; i < source.length; i++)
                {
                    source[i] = new Integer[ facilityLocation.length ];
                    Integer[] tour = ((QapParticle)_particleSwarm.getParticles()[i]).getAssign();
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

    private Particle[] generateInitialSolutions(int length) throws Exception
    {
        Particle[] particles = generateQapRandomParticles( _numParticles, length );

        for(Particle particle : particles)
        {
            // Initial coords
            for(int i = 0; i < length; i++)
                particle.getCoords()[i] = Math.random();

            // Initial velocity
            for(int i = 0; i < length; i++)
                particle.getVelocity()[i] = Math.random();

            // Evaluate
            _objectiveFunction.setObjectiveValue( particle );
        }

        return particles;
    }

//    void printArray(Integer[] array)
//    {
//        for(int i = 0; i< array.length; i++)
//            System.out.print(" " + array[i]);
//    }

    private Particle[] generateQapRandomParticles(int count, int length)
    {
        QapRandomParticle[] particles = new QapRandomParticle[count];
        for(int i = 0; i < count; i++)
            particles[i] = new QapRandomParticle( length );

        return particles;
    }
}
