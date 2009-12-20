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
        findBestHunger(cities);
    }

    private void findBestHunger(City[] cities) {
        double smaller = initHungerTour(cities, 0);
        double act = 0;
        int indexSmaller = 0;
        for (int i = 1; i < cities.length; i++) {
            act = initHungerTour(cities, i);
            if(act < smaller){
                smaller = act;
                indexSmaller = i;
            }
        }
        initHungerTour(cities, indexSmaller);
        System.out.println("smaller: "+smaller);
    }

    private double initHungerTour(City[] c, int listIndex) {
        List<City> openList = new ArrayList();
        for (int i = 0; i < c.length; i++) {
            openList.add(c[i]);
        }

        List<City> hungerTour = new ArrayList();
        City actCity = openList.get(listIndex);
        hungerTour.add(actCity);
        openList.remove(listIndex);
        double totalDist = 0;

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
            totalDist += distance;
        }
        totalDist += euklDist(hungerTour.get(0), hungerTour.get(hungerTour.size() - 1));

        this._tour = new Integer[c.length];
        for (int i = 0; i < c.length; i++) {
            this._tour[i] = hungerTour.get(i).ID - 1;
        }

        System.out.println("total distance: " + totalDist);
        return totalDist;
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
