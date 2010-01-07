/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Richard Malek
 */
public class TourProvider
{
    public static void main(String[] args)
    {
        try
        {
            if(args.length == 0)
                throw new Exception("Usage: java org.seage.problem.tsp.TourProvider {data-tsp-path}");

            long t0 = System.currentTimeMillis();
            
            System.out.println("Instance: "+args[0]);

            City[] cities = CityProvider.readCities(args[0]);

            System.out.println("Cities: "+cities.length);
            System.out.println();

            System.out.println("Read: "+(System.currentTimeMillis() - t0)  + " ms");
            t0 = System.currentTimeMillis();


            Integer[] tour = createGreedyTour(cities);
            System.out.println("Creation: "+(System.currentTimeMillis() - t0) / 1000 + " s");
            t0 = System.currentTimeMillis();

            double tourLenght = getTourLenght(tour, cities);
            System.out.println("Evaluation: "+(System.currentTimeMillis() - t0) + " ms");
            t0 = System.currentTimeMillis();

            Visualizer.instance().createGraph(cities, tour, "tour.png", 1000, 1000);
            System.out.println("Visualization: "+(System.currentTimeMillis() - t0) + " ms");

            System.out.println();
            System.out.println("Tour lenght: "+tourLenght);
            System.out.println("Time: "+(System.currentTimeMillis() - t0) / 1000 + " s");


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static Integer[] createGreedyTour(City[] cities) throws Exception
    {
        Integer[] tour = new Integer[cities.length];

        // Greedy neighbor initialize
        int[] avail = new int[ cities.length ];

        for( int i = 0; i < avail.length; i++ )
        {
            tour[i] = 0;
            avail[i] = i;
        }
        tour[0] = (int)(Math.random() * (cities.length-1));
        for( int i = 1; i < tour.length; i++ )
        {
            int closest = -1;
            double dist = Double.MAX_VALUE;
            for( int j = 1; j < avail.length; j++ )
                if( (norm(cities, tour[i-1], j ) < dist) && (avail[j] >= 0) )
                {
                    dist = norm(cities, tour[i-1], j );
                    closest = j;
                }   // end if: new nearest neighbor
            tour[i] = closest;
            avail[closest] = -1;
        }   // end for

        return tour;
    }

    // TODO: A - create better implementation
    public static Integer[] createRandomTour(City[] cities)
    {
        Integer[] tour = new Integer[ cities.length ];
        Random random = new Random();
        List<Integer> listTour = new ArrayList();
        for (int i = 0; i < cities.length; i++) {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList();
        Integer pom = null;
        for (int i = 0; i < cities.length; i++) {
            pom = listTour.get(random.nextInt(listTour.size()));
            randTour.add(pom);
            listTour.remove(pom);
        }

        for (int i = 0; i < cities.length; i++) {
            tour[i] = randTour.get(i);
        }

        return tour;
    }

    public static Integer[] createSortedTour(City[] cities)
    {
        Integer[] tour = new Integer[ cities.length ];
        for(int i = 0; i < tour.length; i++)
            tour[i] = i;

        return tour;
    }

    private static double norm( City[]matr, int a, int b )
    {
        double xDiff = matr[b].X - matr[a].X;
        double yDiff = matr[b].Y - matr[a].Y;
        return Math.sqrt( xDiff*xDiff + yDiff*yDiff );
    }   // end norm

    public static double getTourLenght(Integer[] tour, City[] cities) throws Exception
    {
        double lenght = 0, dx, dy;
        for (int i = 0; i < tour.length-1; i++) {
            dx = cities[tour[i]].X - cities[tour[i+1]].X;
            dy = cities[tour[i]].Y - cities[tour[i+1]].Y;
            lenght += Math.sqrt(dx * dx + dy * dy);
        }
        dx = cities[tour[0]].X - cities[tour[tour.length-1]].X;
        dy = cities[tour[0]].Y - cities[tour[tour.length-1]].Y;
        lenght += Math.sqrt(dx * dx + dy * dy);
        return lenght;
    }
}
