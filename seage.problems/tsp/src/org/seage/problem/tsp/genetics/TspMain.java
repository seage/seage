package org.seage.problem.tsp.genetics;

import java.util.Random;
import org.seage.metaheuristic.genetics.GeneticSearch;
import org.seage.metaheuristic.genetics.GeneticSearchEvent;
import org.seage.metaheuristic.genetics.GeneticSearchListener;
import org.seage.metaheuristic.genetics.Genome;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.tsp.data.City;
import org.seage.problem.tsp.data.CityProvider;

/**
 *
 * @author rick
 */
public class TspMain implements GeneticSearchListener
{
    public static void main(String[] args)
    {
        try
        {
            new TspMain().run(args[0]);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        City[] cities = CityProvider.readCities(path);
        
        GeneticSearch gs = new GeneticSearch(new TspGeneticOperator(), new TspEvaluator(cities));     
        gs.addGeneticSearchListener(this);
        gs.setEliteSubjectPct(0.05);
        gs.setMutateSubjectPct(0.25);
        gs.setPopulationCount(500);
        gs.setRandomSubjectPct(0.05);
        gs.setCrossLengthPct(0.40);
        gs.setIterationToGo(500);
        gs.startSearching(generateInitialSubjects(cities.length, 100));
    }
    private Subject[] generateInitialSubjects(int length, int count)
    {
        int numTours = count;
        int tourLenght = length;
        Subject[] result = new Subject[numTours];

        Random r = new Random();

        for(int k=0;k<numTours;k++)
        {
            Integer[] newTour = new Integer[tourLenght];

            for (int i = 0; i < tourLenght; i++)
            {
                newTour[i] = i;
            }

            for (int i = 0; i < tourLenght*10; i++)
            {
                int a = r.nextInt(length);
                int b = r.nextInt(length);
                // swap
                int t = newTour[a];
                newTour[a] = newTour[b];
                newTour[b] = t;
            }

            Subject s = new Subject(new Genome(1, tourLenght));
            s.getGenome().getChromosome(0).setGeneArray(newTour);

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
