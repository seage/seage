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
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.qap.fireflies;

import java.util.logging.Logger;

import org.seage.metaheuristic.fireflies.FireflySearch;
import org.seage.metaheuristic.fireflies.FireflySearchEvent;
import org.seage.metaheuristic.fireflies.FireflySearchListener;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.problem.qap.FacilityLocationProvider;

/**
 *
 * @author Richard Malek
 */
public class QapFireflyAlgorithmTest implements FireflySearchListener
{
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger("org.seage");
    private static String _dataPath = "data/chr12a.dat";
    //    private static String _dataPath = "D:\\qap\\bur26a.dat";
    public FireflySearch fs;
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
            System.out.println("Average:" + (double) sum / (double) iters);
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

        fs = new FireflySearch(qfo, qof);
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
        System.out.println("Length of solution" + (new QapRandomSolution(facilityLocations)._assign.length));
        fs.startSolving(generateInitialSolutions(facilityLocations, populationSize));
    }

    private Solution[] generateInitialSolutions(Double[][][] fl, int count) throws Exception
    {
        Solution[] result = new Solution[count];
        //        result[0]=new QapGreedyStartSolution(fl);
        for (int i = 0; i < count; i++)
        {
            result[i] = new QapRandomSolution(fl);
        }
        return result;
    }

    public void FireflySearchStarted(FireflySearchEvent e)
    {
        System.out.println("Firefly Algorithm for QAP started.");
    }

    public void FireflySearchStopped(FireflySearchEvent e)
    {
        System.out.println("Firefly Algorithm for QAP stopped.");
    }

    public void newBestSolutionFound(FireflySearchEvent e)
    {
        System.out.println("New best: " + e.getFireflySearch().getBestSolution().getObjectiveValue()[0]);
    }

    public void noChangeInValueIterationMade(FireflySearchEvent e)
    {
        System.out.println("Firefly Algorithm for QAP - no change in value iteration made.");
    }

    public void newCurrentSolutionFound(FireflySearchEvent e)
    {
        System.out.println("Firefly Algorithm for QAP - new current solution found.");
    }

    public void unimprovingMoveMade(FireflySearchEvent e)
    {
        System.out.println("Firefly Algorithm for QAP - unimproving move made.");
    }

    public void improvingMoveMade(FireflySearchEvent e)
    {
        System.out.println("Firefly Algorithm for QAP - improving move made.");
    }

    public void noChangeInValueMoveMade(FireflySearchEvent e)
    {
        System.out.println("Firefly Algorithm for QAP - no change in value made.");
    }
}
