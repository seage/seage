/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.sat.hillclimber;

import java.util.ArrayList;
import java.util.List;
import org.seage.problem.sat.Clause;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatGreedySolution extends SatSolution {

    /**
     * Constructor the solution with using the greedy algorithm for initial solution
     * @param cities - List of cities with their coordinates
     */
    public SatGreedySolution(Clause[] clauses) {
        super();
        //initGreedyTour(clauses, _rnd.nextInt(clauses.length));
    }

//
//    private double initGreedyTour(City[] c, int listIndex) {
//        List<City> openList = new ArrayList();
//
//        /*Moving the cities in ArrayList for a next processing*/
//        for (int i = 0; i < c.length; i++) {
//            openList.add(c[i]);
//        }
//
//        List<City> hungerTour = new ArrayList();
//        City actCity = openList.get(listIndex);
//        hungerTour.add(actCity);
//        openList.remove(listIndex);
//        double totalDist = 0;
//
//        /*Cycle the greedy algorithm*/
//        while (openList.size() != 0) {
//            double distance = Double.MAX_VALUE, aktDistance;
//            for (int i = 0; i < openList.size(); i++) {
//                aktDistance = euklDist(openList.get(i), actCity);
//                if (aktDistance < distance) {
//                    distance = aktDistance;
//                    listIndex = i;
//                }
//            }
//            actCity = openList.get(listIndex);
//            hungerTour.add(actCity);
//            openList.remove(listIndex);
//            totalDist += distance;
//        }
//
//        totalDist += euklDist(hungerTour.get(0), hungerTour.get(hungerTour.size() - 1));
//        _tour = new Integer[c.length];
//
//        /*Transform the list of cities, generated wiht greedy algorithm, in array IDs*/
//        for (int i = 0; i < c.length; i++) {
//            _tour[i] = hungerTour.get(i).ID - 1;
//        }
//
//        return totalDist;
//    }
//

    private int valueOfClause(Clause clause, Literal[] myLiterals) {
        Literal[] literals = clause.getLiterals();
        for(int i = 0; i < 3; i++){
            //konec
        }
        return 0;
    }
}
