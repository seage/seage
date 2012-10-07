/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp;

import java.io.FileInputStream;
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

            City[] cities = CityProvider.readCities(new FileInputStream(args[0]));

            System.out.println("Cities: "+cities.length);
            System.out.println();

            System.out.println("Read: "+(System.currentTimeMillis() - t0)  + " ms");
            t0 = System.currentTimeMillis();


            Integer[] tour = createGreedyTour(cities, 1);
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

    public static Integer[] createGreedyTour(City[] cities, long randomSeed) throws Exception
    {
        Random r = new Random(randomSeed);
        Integer[] tour = new Integer[cities.length];

        // Greedy neighbor initialize
        int[] avail = new int[ cities.length ];

        for( int i = 0; i < avail.length; i++ )
        {
            tour[i] = 0;
            avail[i] = i;
        }
        tour[0] = (int)(r.nextInt(cities.length));
        avail[tour[0]] = -1;
        for( int i = 1; i < tour.length; i++ )
        {
            int closest = -1;
            double dist = Double.MAX_VALUE;
            for( int j = 0; j < avail.length; j++ )
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
        List<Integer> listTour = new ArrayList<Integer>();
        for (int i = 0; i < cities.length; i++) {
            listTour.add(i);
        }

        List<Integer> randTour = new ArrayList<Integer>();
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
