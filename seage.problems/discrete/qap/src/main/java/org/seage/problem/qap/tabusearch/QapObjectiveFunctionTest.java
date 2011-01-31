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
package org.seage.problem.qap.tabusearch;

import org.seage.problem.qap.FacilityLocationProvider;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;

/**
 *
 * @author Karel Durkota
 */
public class QapObjectiveFunctionTest implements TabuSearchListener
{

    private static String _dataPath = "D:\\esc16i.dat";
//    private static Integer[] assign = {0,1,5,3,7,2,6,4,13,12,15,14,8,10,9,11};

    private static Integer[] assign = {1, 3, 2, 4, 6, 8, 7, 8, 6, 5, 14 ,13, 16, 15, 9, 11, 10, 12};

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
        Double[][][] facilityLocation = FacilityLocationProvider.readFacilityLocations(path);
        System.out.println("Loading an instance from path: " + path);
        System.out.println("Number of cities: " + facilityLocation[0].length);

        QapObjectiveFunction qof = new QapObjectiveFunction(facilityLocation);
        System.out.println(qof.evaluate(new QapSolution(assign), null)[0]);
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
