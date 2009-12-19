package org.seage.problem.tsp.tabusearch;

import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;
import org.seage.problem.tsp.data.*;

/**
 *
 * @author Richard Malek
 */
public class TspMain implements TabuSearchListener
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
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + cities.length);

        System.out.println("Initiate TSPClient");
        TabuSearch ts = new TabuSearch(new TspGreedyStartSolution(cities),
                new TspMoveManager(),
                new TspObjectiveFunction(cities),
                new SimpleTabuList(50),
                new BestEverAspirationCriteria(),
                new TspLongTermMemory(), false);

        ts.addTabuSearchListener(this);
        ts.setIterationsToGo(10000);
        ts.startSolving();
    }

    public void newBestSolutionFound(TabuSearchEvent e) {
        System.out.println(e.getTabuSearch().getBestSolution().toString());
    }

    public void improvingMoveMade(TabuSearchEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void newCurrentSolutionFound(TabuSearchEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void noChangeInValueMoveMade(TabuSearchEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void tabuSearchStarted(TabuSearchEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void tabuSearchStopped(TabuSearchEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unimprovingMoveMade(TabuSearchEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }


}
