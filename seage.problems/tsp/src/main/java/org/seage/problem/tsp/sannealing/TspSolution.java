/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Jan Zmatlik
 *     - Initial implementation
 *     Richard Malek
 *     - Greedy and Random algorithms
 */
package org.seage.problem.tsp.sannealing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspSolution extends Solution
{
    /**
     * Represent order of cities
     */
    private Integer[] _tour;

    /**
     * Array of cities
     */
    private City[] _cities;

    private Random _random = new Random();

    public TspSolution(City[] cities)
    {
        this( cities , SolutionType.SORTED );
    }

    public TspSolution(City[] cities, Solution.SolutionType solutionType)
    {
        _tour = new Integer[cities.length];
        _cities = cities;

        switch( solutionType )
        {
            case SORTED:
                initSortedTour();
                break;
            case GREEDY:
                initGreedyTour();
                break;
            case RANDOM:
                initRandomTour();
                break;
        }

    }

    /**
     * Sort and fill the tour
     */
    private void initSortedTour()
    {
        int tourLength = _tour.length;
        for(int i = 0; i < tourLength; i++) _tour[i] = i;
        System.out.println("SORTED");
    }

    private void initGreedyTour()
    {
        int listIndex = _random.nextInt( _cities.length );
        List<City> openList = new ArrayList();

        for (int i = 0; i < _cities.length; i++) {
            openList.add(_cities[i]);
        }

        List<City> hungerTour = new ArrayList();
        City actCity = openList.get(listIndex);
        hungerTour.add(actCity);
        openList.remove(listIndex);
        //double totalDist = 0;

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
            //totalDist += distance;
        }
        //totalDist += euklDist(hungerTour.get(0), hungerTour.get(hungerTour.size() - 1));

        _tour = new Integer[_cities.length];
        for (int i = 0; i < _cities.length; i++) {
            _tour[i] = hungerTour.get(i).ID - 1;
        }
    }

    private void initRandomTour()
    {
        List<Integer> listTour = new ArrayList();
        for (int i = 0; i < _cities.length; i++) {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList();
        Integer pom = null;
        for (int i = 0; i < _cities.length; i++) {
            pom = listTour.get(_random.nextInt(listTour.size()));
            randTour.add(pom);
            listTour.remove(pom);
        }

        _tour = new Integer[_cities.length];
        for (int i = 0; i < _cities.length; i++) {
            _tour[i] = randTour.get(i);
        }
    }

    private double euklDist(City c1, City c2)
    {
        double dx = c1.X - c2.X;
        double dy = c1.Y - c2.Y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Integer[] getTour()
    {
        return _tour;
    }

     public void setTour(Integer[] tour)
    {
        _tour = tour;
    }

    public City[] getCities()
    {
        return _cities;
    }
    
    public void setCities(City[] cities)
    {
        _cities = cities;
    }

    @Override
    public TspSolution clone()
    {
        TspSolution tspSolution = null;
        try
        {
            tspSolution = (TspSolution)super.clone();
            tspSolution.setTour( this._tour.clone() );
            tspSolution.setCities( this._cities.clone() );
        } catch (CloneNotSupportedException ex)
        {
            Logger.getLogger(TspSolution.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tspSolution;
    }

}
