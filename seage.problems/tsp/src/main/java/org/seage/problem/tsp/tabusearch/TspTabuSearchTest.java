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
package org.seage.problem.tsp.tabusearch;

import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.City;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;

/**
 *
 * @author Richard Malek
 */
public class TspTabuSearchTest implements TabuSearchListener
{
    public static void main(String[] args)
    {
        try
        {
            new TspTabuSearchTest().run(args[0]);
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
