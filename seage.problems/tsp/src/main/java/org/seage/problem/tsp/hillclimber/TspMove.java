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
package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class TspMove implements IMove {

    /**
     * _ix1, _ix2 - Indices of the cities that will be swapped
     */
    int _ix1;
    int _ix2;

    /*
     * Constructor the object, which defines the elementary step of the algorithm
     */
    public TspMove(int ix1, int ix2) {
        _ix1 = ix1;
        _ix2 = ix2;
    }

    /**
     * Function for geting one index
     * @return - First index
     */
    public int getIx1() {
        return _ix1;
    }

    /**
     * Function for geting second index
     * @return - Second index
     */
    public int getIx2() {
        return _ix2;
    }

    /**
     * Function for the applycation the actual solution as new solution
     * @param s - Actual solution
     * @return - New solution
     */
    public Solution apply(Solution s) {

        /*Applycation for the random solution*/
        if (s.switcher.equals("Random") || s.switcher.equals("random")) {
            TspRandomSolution newSol = (TspRandomSolution) s;
            Integer[] newTour = newSol.getTour();
            int pom = newTour[_ix1];
            newTour[_ix1] = newTour[_ix2];
            newTour[_ix2] = pom;
            newSol.setTour(newTour);
            return (Solution) newSol;
        }

        /*Applycation for the greedy solution*/
        if (s.switcher.equals("Greedy") || s.switcher.equals("greedy")) {
            TspGreedySolution newSol = (TspGreedySolution) s;
            Integer[] newTour = newSol.getTour();
            int pom = newTour[_ix1];
            newTour[_ix1] = newTour[_ix2];
            newTour[_ix2] = pom;
            newSol.setTour(newTour);
            return (Solution) newSol;
        }
        
        return null;
    }
}
