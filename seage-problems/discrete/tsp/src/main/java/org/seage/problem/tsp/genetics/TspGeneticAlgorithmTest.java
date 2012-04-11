/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.genetics;

import java.io.FileInputStream;
import org.seage.metaheuristic.genetics.GeneticSearch;
import org.seage.metaheuristic.genetics.GeneticSearchEvent;
import org.seage.metaheuristic.genetics.GeneticSearchListener;
import org.seage.metaheuristic.genetics.Genome;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TourProvider;

/**
 *
 * @author Richard Malek
 */
public class TspGeneticAlgorithmTest implements GeneticSearchListener
{
    public static void main(String[] args)
    {
        try
        {
            new TspGeneticAlgorithmTest().run(args[0]);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        City[] cities = CityProvider.readCities(new FileInputStream(path));
        
        GeneticSearch gs = new GeneticSearch(new TspGeneticOperator(), new TspEvaluator(cities));     
        gs.addGeneticSearchListener(this);
        gs.setEliteSubjectPct(0.05);
        gs.setMutateSubjectPct(0.25);
        gs.setPopulationCount(500);
        gs.setRandomSubjectPct(0.1);
        gs.setCrossLengthPct(0.40);
        gs.setIterationToGo(500);
        gs.startSearching(generateInitialSubjects(cities, 100));
    }

    private Subject[] generateInitialSubjects(City[] cities, int count) throws Exception
    {
        int numTours = count;
        int tourLenght = cities.length;
        Integer[][] tours = new Integer[numTours][tourLenght];
        Subject[] result = new Subject[numTours];

        for(int k=0;k<tours.length;k++)
            if(k<tours.length/20)
                tours[k] = TourProvider.createGreedyTour(cities);
            else
                tours[k] = TourProvider.createRandomTour(cities);

        for(int k=0;k<tours.length;k++)
        {
            Subject s = new Subject(new Genome(1, tourLenght));
            s.getGenome().getChromosome(0).setGeneArray(tours[k]);

            result[k] = s;
        }
        return result;
    }

    public void geneticSearchStarted(GeneticSearchEvent e) {
        System.out.println("Genetic Algorithm for TSP started.");
    }

    public void geneticSearchStopped(GeneticSearchEvent e) {
        System.out.println("Genetic Algorithm for TSP stopped.");
    }

    public void newBestSolutionFound(GeneticSearchEvent e) {
        System.out.println("New best: " + e.getGeneticSearch().getBestSubject().getFitness()[0]);
    }

    public void noChangeInValueIterationMade(GeneticSearchEvent e) {
        
    }
    
    
}
