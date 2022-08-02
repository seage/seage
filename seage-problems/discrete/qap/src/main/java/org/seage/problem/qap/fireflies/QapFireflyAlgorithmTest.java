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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.qap.fireflies;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.fireflies.FireflySearch;
import org.seage.metaheuristic.fireflies.FireflySearchEvent;
import org.seage.metaheuristic.fireflies.FireflySearchListener;
import org.seage.problem.qap.FacilityLocationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Richard Malek
 */
public class QapFireflyAlgorithmTest implements FireflySearchListener<QapSolution>
{
    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger("org.seage");
    private static String _dataPath = "data/chr12a.dat";
    //    private static String _dataPath = "D:\\qap\\bur26a.dat";
    public FireflySearch<QapSolution> fs;
    public static int iters = 2;

    public static void main(String[] args)
    {
        try
        {
            double sum = 0;
            String str = "";
            for (int i = 0; i < iters; i++)
            {
                QapFireflyAlgorithmTest q = new QapFireflyAlgorithmTest();
                q.run(_dataPath);
                str += q.fs.getBestSolution().getObjectiveValue()[0] + "\n";
                sum += q.fs.getBestSolution().getObjectiveValue()[0];
            }
            System.out.println(str);
            System.out.println("Average:" + sum / iters);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        Double[][][] facilityLocations = FacilityLocationProvider.readFacilityLocations(path);

        System.out.println("Loading an instance from path: " + path);
        System.out.println("Number of facilities/location: " + facilityLocations.length + ","
                + facilityLocations[0].length + "," + facilityLocations[0][0].length);

        boolean _withDecreasingRandomness = true;
        boolean _withHillClimbingBestSolution = true;
        boolean _bestSolutionNoMove = false;
        double _initialIntensity = 1;
        double _initialRandomness = 5;
        double _finalRandomness = 2;
        double _absorption = 0.4;//0.025;
        double _timeStep = 1.7;//0.15;
        int populationSize = 1000;
        boolean _maximizing = false;
        int iterationsToGo = 1000;

        QapFireflyOperator qfo = new QapFireflyOperator(_initialIntensity, _initialRandomness, _finalRandomness,
                _absorption, _timeStep, _withDecreasingRandomness);
        QapFireflyOperator._facilityLocations = facilityLocations;
        QapObjectiveFunction qof = new QapObjectiveFunction(facilityLocations);

        fs = new FireflySearch<>(qfo, qof);
        fs.addFireflySearchListener(this);
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
        System.out.println("Length of solution" + (new QapRandomSolution(facilityLocations).assign.length));
        fs.startSolving(generateInitialSolutions(facilityLocations, populationSize));
    }

    private List<QapSolution> generateInitialSolutions(Double[][][] fl, int count) throws Exception
    {
        ArrayList<QapSolution> result = new ArrayList<>(count);
        //        result[0]=new QapGreedyStartSolution(fl);
        for (int i = 0; i < count; i++)
        {
            result.add(new QapRandomSolution(fl));
        }
        return result;
    }

    @Override
    public void FireflySearchStarted(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("Firefly Algorithm for QAP started.");
    }

    @Override
    public void FireflySearchStopped(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("Firefly Algorithm for QAP stopped.");
    }

    @Override
    public void newBestSolutionFound(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("New best: " + e.getFireflySearch().getBestSolution().getObjectiveValue()[0]);
    }

    public void noChangeInValueIterationMade(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("Firefly Algorithm for QAP - no change in value iteration made.");
    }

    @Override
    public void newCurrentSolutionFound(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("Firefly Algorithm for QAP - new current solution found.");
    }

    @Override
    public void unimprovingMoveMade(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("Firefly Algorithm for QAP - unimproving move made.");
    }

    @Override
    public void improvingMoveMade(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("Firefly Algorithm for QAP - improving move made.");
    }

    @Override
    public void noChangeInValueMoveMade(FireflySearchEvent<QapSolution> e)
    {
        System.out.println("Firefly Algorithm for QAP - no change in value made.");
    }
}
