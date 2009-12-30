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
 *     - Greedy algorithm
 */
package org.seage.problem.tsp.sannealing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.seage.problem.tsp.City;

/**
 *
 * @author Jan Zmatlik
 */
public class TspGreedySolution extends TspSolution {

    private Random _random = new Random();

    public TspGreedySolution(City[] cities)
    {
        super( cities );
        initGreedyTour();
    }

    private void initGreedyTour()
    {
        int listIndex = _random.nextInt( _cities.length );
        List<City> openList = new ArrayList();

        for (int i = 0; i < _cities.length; i++) openList.add( _cities[i] );

        List<City> hungerTour = new ArrayList();
        City actCity = openList.get( listIndex );
        hungerTour.add( actCity );
        openList.remove( listIndex );

        while ( openList.size() != 0 )
        {
            double distance = Double.MAX_VALUE, aktDistance;
            for ( int i = 0; i < openList.size(); i++ )
            {
                aktDistance = euklDist( openList.get(i) , actCity );
                if ( aktDistance < distance )
                {
                    distance = aktDistance;
                    listIndex = i;
                }
            }
            actCity = openList.get( listIndex );
            hungerTour.add( actCity );
            openList.remove( listIndex );
        }

        _tour = new Integer[ _cities.length ];
        for ( int i = 0; i < _cities.length; i++ )
        {
            _tour[i] = hungerTour.get(i).ID - 1;
        }
    }


    private double euklDist(City c1, City c2)
    {
        double dx = c1.X - c2.X;
        double dy = c1.Y - c2.Y;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
