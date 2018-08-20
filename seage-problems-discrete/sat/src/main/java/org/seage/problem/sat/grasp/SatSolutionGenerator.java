/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Martin Zaloga - Initial implementation
 */
package org.seage.problem.sat.grasp;

import org.seage.metaheuristic.grasp.ISolutionGenerator;
import org.seage.metaheuristic.grasp.Solution;
import org.seage.problem.sat.Formula;

/**
 *
 * @author Zagy
 */
public class SatSolutionGenerator implements ISolutionGenerator
{

    /**
     * _cities - Loaded cities _switcher - Parameter setting random or greedy
     * initial solution
     */
    private Formula _formula;
    private String _switcher;

    /**
     * The constructor of object TSPSolutionGenerator
     * 
     * @param switcher
     *            - Loaded cities
     * @param cities
     *            - Parameter setting random or greedy initial solution
     */
    public SatSolutionGenerator(String switcher, Formula formula)
    {
        _formula = formula;
        _switcher = switcher;
    }

    /**
     * Function to generate the initial solution
     * 
     * @return
     */
    @Override
    public Solution generateSolution() throws Exception
    {

        SatSolution satSolution = null;

        /* Initialization for the random initial solution */
        if (_switcher.equals("Random") || _switcher.equals("random"))
        {
            satSolution = new SatRandomSolution(_formula);
        }

        /* Initialization for the greedy initial solution */
        if (_switcher.equals("Greedy") || _switcher.equals("greedy"))
        {
            satSolution = new SatGreedySolution(_formula);
        }
        return satSolution;
    }
}
