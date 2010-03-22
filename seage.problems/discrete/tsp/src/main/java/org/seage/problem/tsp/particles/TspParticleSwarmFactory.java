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
//    private TspSolution _tspSolution;
    private City[] _cities;

    private TspObjectiveFunction _objectiveFunction;

    public TspParticleSwarmFactory(DataNode params, City[] cities) throws Exception
    {
        _cities = cities;
//        String solutionType = params.getValueStr("initSolutionType");
//        if( solutionType.toLowerCase().equals("greedy") )
//            _tspSolution = new TspGreedySolution( cities );
//        else if( solutionType.toLowerCase().equals("random") )
//            _tspSolution = new TspRandomSolution( cities );
//        else if( solutionType.toLowerCase().equals("sorted") )
//            _tspSolution = new TspSortedSolution( cities );
    }

    public IAlgorithmAdapter createAlgorithm(DataNode algorithmParams) throws Exception
    {
        IAlgorithmAdapter algorithm;
        _objectiveFunction = new TspObjectiveFunction();

        algorithm = new ParticleSwarmAdapter(
                generateInitialSolutions(),
                _objectiveFunction,
                false, "")
        {
            public void solutionsFromPhenotype(Object[][] source) throws Exception
            {

            }

            public Object[][] solutionsToPhenotype() throws Exception
            {
                return null;
            }
        };
//        {
//
//        algorithm = new ParticleSwarmAdapter((Solution) _tspSolution,
//                new TspObjectiveFunction(),
//                new TspVelocityManager(), false, "")
//        {
//            public void solutionsFromPhenotype(Object[][] source) throws Exception
//            {
//                TspSolution initialSolution = new TspGreedySolution(TspProblemProvider.getCities());
//                Integer[] tour = initialSolution.getTour();
//
//                for(int i = 0; i < tour.length; i++)
//                    tour[i] = (Integer)source[0][i];
//
//                _initialSolution = initialSolution;
//            }
//
//            public Object[][] solutionsToPhenotype() throws Exception
//            {
//                Integer[] tour = ((TspSolution) _simulatedAnnealing.getBestSolution()).getTour();
//                Object[][] source = new Object[1][ tour.length ];
//
//                source[0] = new Integer[ tour.length ];
//                for(int i = 0; i < tour.length; i++)
//                    source[0][i] = tour[i];
//
//                return source;
//            }
//
//        };

        return algorithm;
    }

    private Particle[] generateInitialSolutions() throws Exception
    {
        Particle[] particles = new Particle[]
        {
            new TspRandomParticle(_cities, _cities.length),
            new TspRandomParticle(_cities, _cities.length),
            new TspRandomParticle(_cities, _cities.length)
        };


        for(Particle particle : particles)
        {
            // Initial coords
            for(int i = 0; i < _cities.length; i++)
                particle.getCoords()[i] = Math.random();

            insertionSort(particle.getCoords());
            
            for(int i = 0; i < _cities.length; i++)
                ((TspParticle)particle).getTour()[i] = i;

            // Initial velocity
            for(int i = 0; i < _cities.length; i++)
                particle.getVelocity()[i] = Math.random();

            // Evaluate
            _objectiveFunction.setObjectiveValue( particle );
        }

//        _objectiveFunction.setObjectiveValue(random);
//        _objectiveFunction.setObjectiveValue(random1);
//        _objectiveFunction.setObjectiveValue(random2);


        
//        return new Solution[]{
//            new TspGreedySolution(_cities),
//            new TspRandomSolution(_cities),
//            new TspSortedSolution(_cities)
//        };

        return particles;
    }

    public void insertionSort(double[] array)
    {
        //double[] sortedArray = array.clone();
        for(int i = 0; i < array.length - 1; i++)
        {
            int j = i + 1;
            double tmp = array[j];
            while(j > 0 && tmp > array[j-1])
            {
                array[j] = array[j-1];
                j--;
            }
            array[j] = tmp;
        }
    }
}
