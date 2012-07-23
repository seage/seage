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
package org.seage.problem.rosenbrock.fireflies;

import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.FireflySearch;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.metaheuristic.particles.Particle;
import org.seage.metaheuristic.particles.ParticleSwarm;

/**
 *
 * @author Jan Zmatlik
 */
public class RosenbrockTest
{
    public static void main(String[] args) throws Exception
    {
        boolean _withDecreasingRandomness=false;
        boolean _withHillClimbingBestSolution=false;
        boolean _bestSolutionNoMove=false;
        double _initialIntensity=1;
        double _initialRandomness=5;
        double _finalRandomness=2;
        double _absorption=0.3;//0.025;
        double _timeStep=1.7;//0.15;
        int populationSize = 100;
        boolean _maximizing = false;
        int iterationsToGo = 1000;
        double[] minBound = {-10,-10};
        double[] maxBound = {10,10};

        FireflyOperator fo = new ContinuousFireflyOperator(_initialIntensity,_initialRandomness,_finalRandomness,_absorption,_timeStep,_withDecreasingRandomness,2,minBound,maxBound);
        RosenbrockObjectiveFunction rof = new RosenbrockObjectiveFunction();
                
        FireflySearch fs = new FireflySearch(fo, rof);
//        fs.addFireflySearchListener(this);
        fs.setWithDecreasingRandomness(_withDecreasingRandomness);
        fs.setWithHillClimbingBestSolution(_withHillClimbingBestSolution);
        fs.setInitialIntensity(_initialIntensity);
        fs.setInitialRandomness(_initialRandomness);
        fs.setFinalRandomness(_finalRandomness);
        fs.setBestSolutionNoMove(_bestSolutionNoMove);
        fs.setAbsorption(_absorption);
        fs.setTimeStep(_timeStep);
        fs.setPopulationCount(populationSize);
        fs.setMaximizing(_maximizing);
        fs.setIterationsToGo(iterationsToGo);
//        System.out.println("Length of solution"+(new QapRandomSolution(facilityLocations)._assign.length));
        Solution[] solutions = generateInitialSolutions(10,fo);
        fs.startSolving(solutions);        
    }

    private static Solution[] generateInitialSolutions(int count, FireflyOperator fo) throws Exception
    {
        Solution[] result = new Solution[count];
//        result[0]=new QapGreedyStartSolution(fl);
        for(int i=0;i<count;i++){
            result[i]= fo.randomSolution();
        }
        return result;
    }
}
