/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Karel Durkota
 *     - Initial implementation
 *     Richard Malek
 *     - Added algorithm annotations
 */
package org.seage.problem.qap.particles;

import org.seage.aal.Annotations;
import org.seage.aal.IAlgorithmAdapter;
import org.seage.aal.IAlgorithmFactory;
import org.seage.aal.IProblemProvider;
import org.seage.aal.particles.ParticleSwarmAdapter;
import org.seage.data.DataNode;

import org.seage.metaheuristic.particles.Particle;
import org.seage.problem.qap.QapProblemProvider;


/**
 *
 * @author Karel Durkota
 */
@Annotations.AlgorithmId("ParticleSwarm")
@Annotations.AlgorithmName("Particle Swarm")
public class QapParticleSwarmFactory implements IAlgorithmFactory
{
    private QapProblemProvider _provider;
    //private Double[][][] _facilityLocation;

    private int _numParticles;

    private QapObjectiveFunction _objectiveFunction;

    public QapParticleSwarmFactory() throws Exception
    {
    }

    public void setProblemProvider(IProblemProvider provider) throws Exception {
        _provider = (QapProblemProvider)provider;
    }

    public Class getAlgorithmClass() {
        return ParticleSwarmAdapter.class;
    }

    public IAlgorithmAdapter createAlgorithm(DataNode config) throws Exception
    {
            throw new UnsupportedOperationException();
    }
    
    public IAlgorithmAdapter createAlgorithm2(DataNode config) throws Exception
    {    
        IAlgorithmAdapter algorithm;

        _provider.initProblemInstance(config);
        _objectiveFunction = new QapObjectiveFunction(_provider.getFacilityLocation());

        algorithm = new ParticleSwarmAdapter(
                generateInitialSolutions(),
                _objectiveFunction,
                false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {
                _numParticles = source.length;
                _initialParticles = generateInitialSolutions();
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
                Object[][] source = new Object[ numOfParticles ][ _provider.getFacilityLocation().length ];

                for(int i = 0; i < source.length; i++)
                {
                    source[i] = new Integer[ _provider.getFacilityLocation().length ];
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

    private Particle[] generateInitialSolutions() throws Exception
    {
        Particle[] particles = generateQapRandomParticles( _numParticles );

        for(Particle particle : particles)
        {
            // Initial coords
            for(int i = 0; i < _provider.getFacilityLocation().length; i++)
                particle.getCoords()[i] = Math.random();

            // Initial velocity
            for(int i = 0; i < _provider.getFacilityLocation().length; i++)
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

    private Particle[] generateQapRandomParticles(int count)
    {
        QapRandomParticle[] particles = new QapRandomParticle[count];
        for(int i = 0; i < count; i++)
            particles[i] = new QapRandomParticle( _provider.getFacilityLocation().length );

        return particles;
    }
}
