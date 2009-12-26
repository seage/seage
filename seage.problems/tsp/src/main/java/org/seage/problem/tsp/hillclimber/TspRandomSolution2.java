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
 * @author rick
 */
public class TspRandomSolution2 extends Solution {

    private Integer[] _tour;
    private Random rnd = new Random();

    public TspRandomSolution2(City[] cities) {
            initRandTour(cities);
    }

    private void initRandTour(City[] c) {

        List<Integer> listTour = new ArrayList();
        for (int i = 0; i < c.length; i++) {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList();
        Integer pom = null;
        for (int i = 0; i < c.length; i++) {
            pom = listTour.get(rnd.nextInt(listTour.size()));
            randTour.add(pom);
            listTour.remove(pom);
        }

        _tour = new Integer[c.length];
        for (int i = 0; i < c.length; i++) {
            _tour[i] = randTour.get(i);
        }
    }

    public Integer[] getTour() {
        return _tour;
    }

    public void setTour(Integer[] tour) {
        _tour = tour;
    }
}
