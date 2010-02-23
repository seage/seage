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

import org.seage.metaheuristic.hillclimber.ISolutionGenerator;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.sat.Formula;

/**
 *
 * @author Zagy
 */
public class SatSolutionGenerator implements ISolutionGenerator {

    /**
     * _cities - Loaded cities
     * _switcher - Parameter setting random or greedy initial solution
     */
    private Formula _formula;
    private String _switcher;

    /**
     * The constructor of object TSPSolutionGenerator
     * @param switcher - Loaded cities
     * @param cities - Parameter setting random or greedy initial solution
     */
    public SatSolutionGenerator(String switcher, Formula formula) {
        _formula = formula;
        _switcher = switcher;
    }

    /**
     * Function to generate the initial solution
     * @return
     */
    public Solution generateSolution() throws Exception {

        SatSolution satSolution = null;

        /*Initialization for the random initial solution*/
        if (_switcher.equals("Random") || _switcher.equals("random")) {
            satSolution = new SatRandomSolution(_formula);
        }

        /*Initialization for the greedy initial solution*/
        if (_switcher.equals("Greedy") || _switcher.equals("greedy")) {
            satSolution = new SatGreedySolution(_formula);
        }
        return satSolution;
    }
}
