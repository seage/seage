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
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.qap.tabusearch;

import java.io.FileInputStream;
import org.seage.problem.qap.FacilityLocationProvider;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;

/**
 *
 * @author Karel Durkota
 */
public class QapTabuSearchTest implements TabuSearchListener
{

    private static String _dataPath = "data/tai12a.dat";

    public static void main(String[] args)
    {
        try
        {
            new QapTabuSearchTest().run( _dataPath);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void run(String path) throws Exception
    {
        Double[][][] facilityLocation = FacilityLocationProvider.readFacilityLocations(new FileInputStream(path) );
        System.out.println("Loading an instance from path: " + path);
        System.out.println("Number of cities: " + facilityLocation[0].length);

        TabuSearch ts = new TabuSearch(new QapGreedyStartSolution(facilityLocation),
                new QapMoveManager(),
                new QapObjectiveFunction(facilityLocation),
                new SimpleTabuList(100),
                new BestEverAspirationCriteria(),
                new QapLongTermMemory(), false);

        ts.addTabuSearchListener(this);
        ts.setIterationsToGo(1000000);
        ts.startSolving();
        Integer[] r = ((QapSolution)ts.getBestSolution()).getAssign();
        System.out.println(ts.getBestSolution().getObjectiveValue()[0]);
        for(int i=0;i<r.length;i++){
            System.out.print((r[i]+1)+",");
        }

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
