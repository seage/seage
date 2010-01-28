/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.ISolutionGenerator;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;

/**
 *
 * @author Zagy
 */
public class TSPSolutionGenerator implements ISolutionGenerator {
     private City[] _cities;

    public TSPSolutionGenerator(City[] cities) {
        _cities = cities;
    }

    public Solution generateSolution(String clasic, String switcher) {
        
        TspSolution tspSolution = null;
        
        /*Initialization for the random initial solution*/
        if (switcher.equals("Random") || switcher.equals("random")) {
            tspSolution = new TspRandomSolution(_cities);
        }

        /*Initialization for the greedy initial solution*/
        if (switcher.equals("Greedy") || switcher.equals("greedy")) {
            tspSolution = new TspGreedySolution(_cities);
        }
        return tspSolution;
    }
}
