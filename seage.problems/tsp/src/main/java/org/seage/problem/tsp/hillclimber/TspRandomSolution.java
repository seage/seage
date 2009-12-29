/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.hillclimber;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author Martin Zaloga
 */
public class TspRandomSolution extends Solution {

    /**
     * _tour - Contains the tour of the solution
     * _rnd - Variable for the purposes of generating the random numbers
     */
    private Integer[] _tour;
    private Random _rnd = new Random();

    /**
     * Constructor the solution with using the random algorithm for initial solution
     * @param cities - List of cities with their coordinates
     */
    public TspRandomSolution(City[] cities) {
            initRandTour(cities);
    }

    /**
     * Creating the initial random solution
     * @param c - The input list of cities
     */
    private void initRandTour(City[] c) {
        List<Integer> listTour = new ArrayList();

        /*Moving the cities in ArrayList for a next processing*/
        for (int i = 0; i < c.length; i++) {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList();
        Integer pom = null;

        /*Cycle the random algorithm*/
        for (int i = 0; i < c.length; i++) {
            pom = listTour.get(_rnd.nextInt(listTour.size()));
            randTour.add(pom);
            listTour.remove(pom);
        }

        _tour = new Integer[c.length];

        /*Transform the list of cities, generated wiht random algorithm, in array IDs*/
        for (int i = 0; i < c.length; i++) {
            _tour[i] = randTour.get(i);
        }
    }

    /**
     * Function for getting tour
     * @return - actual tour
     */
    public Integer[] getTour() {
        return _tour;
    }

    /**
     * Method for setting tour
     * @param tour - Setting tour
     */
    public void setTour(Integer[] tour) {
        _tour = tour;
    }
}
