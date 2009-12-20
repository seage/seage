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
public class TspSolution extends Solution {

    private Integer[] _tour;
    private Random rnd = new Random();

    public TspSolution(City[] cities) {
        initHungerTour(cities);
    }

    private void initHungerTour(City[] c) {
        List<City> openList = new ArrayList();
        for (int i = 0; i < c.length; i++) {
            openList.add(c[i]);
        }

        List<City> hungerTour = new ArrayList();
        int listIndex = rnd.nextInt(openList.size() - 1);
        City actCity = openList.get(listIndex);
        hungerTour.add(actCity);
        openList.remove(listIndex);

        while (openList.size() != 0) {
            double distance = Double.MAX_VALUE, aktDistance;
            for (int i = 0; i < openList.size(); i++) {
                aktDistance = euklDist(openList.get(i), actCity);
                if (aktDistance < distance) {
                    distance = aktDistance;
                    listIndex = i;
                }
            }
            actCity = openList.get(listIndex);
            hungerTour.add(actCity);
            openList.remove(listIndex);
        }

        this._tour = new Integer[c.length];
        for (int i = 0; i < c.length; i++) {
            this._tour[i] = hungerTour.get(i).ID - 1;
        }
    }

    private double euklDist(City c1, City c2) {
        double dx = c1.X - c2.X;
        double dy = c1.Y - c2.Y;
        return Math.sqrt(dx * dx + dy * dy);
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

        this._tour = new Integer[c.length];
        for (int i = 0; i < c.length; i++) {
            this._tour[i] = randTour.get(i);
        }
    }

    public Integer[] getTour() {
        return _tour;
    }

    public void setTour(Integer[] tour) {
        this._tour = tour;
    }
}
