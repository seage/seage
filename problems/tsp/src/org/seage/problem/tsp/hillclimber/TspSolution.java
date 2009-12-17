/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.data.City;

/**
 *
 * @author rick
 */
public class TspSolution extends Solution {

    int[] _tour;

    public TspSolution(City[] cities) {
        initTour(cities);
    }

    private void initTour(City[] c) {
        this._tour = new int[c.length];
        for (int i = 0; i < c.length; i++) {
            this._tour[i] = i;
        }
    }

    public int[] getTour() {
        return _tour;
    }
}
