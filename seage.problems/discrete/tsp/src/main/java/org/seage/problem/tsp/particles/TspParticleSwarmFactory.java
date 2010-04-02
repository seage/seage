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
import org.seage.metaheuristic.particles.ParticleSwarmEvent;
import org.seage.problem.tsp.City;


/**
 *
 * @author Jan Zmatlik
 */
public class TspParticleSwarmFactory implements IAlgorithmFactory
{
//    private TspSolution _tspSolution;
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
//                TspSolution initialSolution = new TspGreedySolution(TspProblemProvider.getCities());
//                Integer[] tour = initialSolution.getTour();
//
//                for(int i = 0; i < tour.length; i++)
//                    tour[i] = (Integer)source[0][i];
//
//                _initialSolution = initialSolution;
            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                Integer[] tour = ((TspParticle)_particleSwarm.getBestParticle()).getTour();
                Object[][] source = new Object[1][ tour.length ];

                source[0] = new Integer[ tour.length ];
                for(int i = 0; i < tour.length; i++)
                    source[0][i] = tour[i];

                System.out.println("BEST TOUR: ");
                printArray(tour);
                System.out.println("");
                
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
            
            for(int i = 0; i < _cities.length; i++)
                ((TspParticle)particle).getTour()[i] = i;

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
