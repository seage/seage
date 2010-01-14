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
import org.seage.aal.particles.ParticleSwarmOptimizationAdapter;
import org.seage.data.DataNode;
import org.seage.metaheuristic.particles.ParticleSwarmOptimizationEvent;
import org.seage.metaheuristic.particles.Solution;
import org.seage.problem.tsp.City;


/**
 *
 * @author Jan Zmatlik
 */
public class TspParticleSwarmOptimizationgFactory implements IAlgorithmFactory
{
//    private TspSolution _tspSolution;
    private City[] _cities;

    public TspParticleSwarmOptimizationgFactory(DataNode params, City[] cities) throws Exception
    {
        _cities = cities;
        System.out.println("JOJO");
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

        algorithm = new ParticleSwarmOptimizationAdapter(
                generateInitialSolutions(),
                new TspObjectiveFunction(),
                new TspVelocityManager(), false, "")
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
//        algorithm = new ParticleSwarmOptimizationAdapter((Solution) _tspSolution,
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

    private Solution[] generateInitialSolutions() throws Exception
    {
        return new Solution[]
        {
            new TspGreedySolution(_cities),
            new TspRandomSolution(_cities),
            new TspSortedSolution(_cities)
        };
    }
}
