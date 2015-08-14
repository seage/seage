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
package org.seage.problem.tsp.genetics;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TourProvider;

/**
 *
 * @author Richard Malek
 */
public class TspGeneticAlgorithmTest implements IAlgorithmListener<GeneticAlgorithmEvent<Subject<Integer>>>
{
    public static void main(String[] args)
    {
        try
        {
            String path = "data/tsp/eil51.tsp";//args[0];		// 426
            //String path = "data/tsp/berlin52.tsp";//args[0]; 	// 7542
            //String path = "data/tsp/ch130.tsp";//args[0]; 		// 6110
            //String path = "data/tsp/lin318.tsp";//args[0]; 		// 42029
            //String path = "data/tsp/pcb442.tsp";//args[0]; 		// 50778
            //String path = "data/tsp/u574.tsp";//args[0]; 		// 36905

            new TspGeneticAlgorithmTest().run(path);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        City[] cities = CityProvider.readCities(new FileInputStream(path));

        GeneticAlgorithm<Subject<Integer>> gs = new GeneticAlgorithm<Subject<Integer>>(new TspGeneticOperator(),
                new TspEvaluator(cities));
        gs.addGeneticSearchListener(this);
        gs.setEliteSubjectsPct(5);
        gs.setMutatePopulationPct(5);
        gs.setMutateChromosomeLengthPct(30);
        gs.setPopulationCount(500);
        gs.setRandomSubjectsPct(5);
        gs.setCrossLengthPct(30);
        gs.setIterationToGo(100000);
        gs.startSearching(generateInitialSubjects(cities, 100));
    }

    private List<Subject<Integer>> generateInitialSubjects(City[] cities, int count) throws Exception
    {
        int numTours = count;
        int tourLenght = cities.length;
        //Integer[][] tours = new Integer[numTours][tourLenght];
        ArrayList<Subject<Integer>> result = new ArrayList<Subject<Integer>>(numTours);

        for (int k = 0; k < tourLenght; k++)
            result.add(new Subject<Integer>(TourProvider.createRandomTour(cities.length)));
        //tours[k] = TourProvider.createRandomTour(cities);

        //        for(int k=0;k<tours.length;k++)
        //        {
        //            Subject s = new Subject(new Genome(1, tourLenght));
        //            s.getChromosome().setGeneArray(tours[k]);
        //
        //            result[k] = s;
        //        }
        return result;
    }

    @Override
    public void algorithmStarted(GeneticAlgorithmEvent<Subject<Integer>> e)
    {
        System.out.println("Genetic Algorithm for TSP started.");
    }

    @Override
    public void algorithmStopped(GeneticAlgorithmEvent<Subject<Integer>> e)
    {
        System.out.println("Genetic Algorithm for TSP stopped.");
    }

    @Override
    public void newBestSolutionFound(GeneticAlgorithmEvent<Subject<Integer>> e)
    {
        System.out.println("New best: " + e.getGeneticSearch().getBestSubject().getFitness()[0]);
    }

    @Override
    public void noChangeInValueIterationMade(GeneticAlgorithmEvent<Subject<Integer>> e)
    {

    }

    @Override
    public void iterationPerformed(GeneticAlgorithmEvent<Subject<Integer>> e)
    {

    }

}
