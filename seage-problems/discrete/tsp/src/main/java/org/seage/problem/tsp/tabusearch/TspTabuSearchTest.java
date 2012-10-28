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
package org.seage.problem.tsp.tabusearch;

import java.io.FileInputStream;
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
    /**
	 * 
	 */
	private static final long serialVersionUID = -6437822073873203681L;

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
        City[] cities = CityProvider.readCities(new FileInputStream(path));
        System.out.println("Loading cities from path: " + path);
        System.out.println("Number of cities: " + cities.length);

        TabuSearch ts = new TabuSearch(new TspGreedyStartSolution(cities),
                new TspMoveManager(),
                new TspObjectiveFunction(cities),
                new SimpleTabuList(50),
                new BestEverAspirationCriteria(),
                false);

        ts.addTabuSearchListener(this);
        ts.setIterationsToGo(100000);
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
