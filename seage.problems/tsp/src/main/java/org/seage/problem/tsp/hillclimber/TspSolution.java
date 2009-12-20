/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author rick
 */
public class TspSolution extends Solution {

    private Integer[] _tour;

    public TspSolution(City[] cities) {
        initTour(cities);
    }

    private void initTour(City[] c) {
        this._tour = new Integer[c.length];
        for (int i = 0; i < c.length; i++) {
            this._tour[i] = i;
        }
    }

    public Integer[] getTour() {
        return _tour;
    }

    public void setTour(Integer[] tour) {
         this._tour = tour;
    }
}
