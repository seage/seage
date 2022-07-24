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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.problem.tsp;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TourProvider with visualization.
 * @author Richard Malek
 */
public class TourProvider {
  private static Logger logger = LoggerFactory.getLogger(TourProvider.class.getName());

  /**. */
  public static Integer[] createGreedyTour(City[] cities, long randomSeed) throws Exception {
    Random rand = new Random(randomSeed);
    Integer[] tour = new Integer[cities.length];

    // Greedy neighbor initialize
    boolean[] avail = new boolean[cities.length];

    for (int i = 0; i < avail.length; i++) {
      tour[i] = 1;
      avail[i] = true;
    }
    tour[0] = rand.nextInt(cities.length) + 1;
    avail[tour[0] - 1] = false;
    for (int i = 1; i < tour.length; i++) {
      // if (i%10000 == 0) logger.info(i);
      int minDistId = -1;
      double minDist = Double.MAX_VALUE;
      for (int j = 0; j < avail.length; j++) {
        double dist = norm(cities, tour[i - 1] - 1, j);
        if (dist < minDist && avail[j]) {
          minDist = dist;
          minDistId = j + 1;
        }
      }
      tour[i] = minDistId;
      avail[minDistId - 1] = false;
    }

    return tour;
  }

  /**. */
  public static Integer[] createRandomTour(int length) {
    Random random = new Random();
    List<Integer> listTour = new ArrayList<Integer>();
    for (int i = 0; i < length; i++) {
      listTour.add(i + 1);
    }
    
    for (int i = 0; i < length; i++) {
      int ix1 = random.nextInt(listTour.size());
      int ix2 = random.nextInt(listTour.size());
      Integer tmp = listTour.get(ix2);
      listTour.set(ix2, listTour.get(ix1));
      listTour.set(ix1, tmp);
    }

    return listTour.toArray(new Integer[0]);
  }

  /**
   * Tour created by ascending citie indexes.
   * @param length .
   * @return
   */
  public static Integer[] createSortedTour(int length) {
    Integer[] tour = new Integer[length];
    for (int i = 0; i < tour.length; i++) {
      tour[i] = i + 1;
    }

    return tour;
  }

  private static double norm(City[] cities, int a, int b) {
    double deltaX = cities[b].X - cities[a].X;
    double deltaY = cities[b].Y - cities[a].Y;
    return deltaX * deltaX + deltaY * deltaY;
  }

  /**. */
  public static double getTourLenght(Integer[] tour, City[] cities) throws Exception {
    double lenght = 0;
    double dx = 0;
    double dy = 0;
    for (int i = 0; i < tour.length - 1; i++) {
      dx = cities[tour[i] - 1].X - cities[tour[i + 1] - 1].X;
      dy = cities[tour[i] - 1].Y - cities[tour[i + 1] - 1].Y;
      lenght += Math.sqrt(dx * dx + dy * dy);
    }
    dx = cities[tour[0] - 1].X - cities[tour[tour.length - 1] - 1].X;
    dy = cities[tour[0] - 1].Y - cities[tour[tour.length - 1] - 1].Y;
    lenght += Math.sqrt(dx * dx + dy * dy);
    return lenght;
  }
  
  ///////////////////////////////////////////////////////////////////////////////////////
  
  /**. */
  public static void main(String[] args) {
    try {
      if (args.length == 0) {
        throw new Exception("Usage: java org.seage.problem.tsp.TourProvider {data-tsp-path}");
      }
      File path = new File(args[0]);
      logger.info("Instance path: " + path.getPath());
            
      long t0 = System.currentTimeMillis();
      City[] cities = CityProvider.readCities(new FileInputStream(args[0]));
      logger.info("Cities: " + cities.length);
      logger.info("Read: " + (System.currentTimeMillis() - t0) + " ms");

      t0 = System.currentTimeMillis();
      Integer[] tour = createGreedyTour(cities, System.currentTimeMillis());
      // Integer[] tour = createRandomTour(cities.length);
      // Integer[] tour = createSortedTour(cities.length);
      logger.info("Creation: " + (System.currentTimeMillis() - t0) / 1000 + " s");

      t0 = System.currentTimeMillis();
      double tourLenght = getTourLenght(tour, cities);
      logger.info("Evaluation: " + (System.currentTimeMillis() - t0) + " ms");
      logger.info("Tour lenght: " + tourLenght);

      t0 = System.currentTimeMillis();
      String imgPath = "output/" + path.getName() + "_" + System.currentTimeMillis() + ".png";
      TspVisualizer.createTourImage(cities, tour, imgPath, 1100, 1100);
      logger.info("Visualization: " + (System.currentTimeMillis() - t0) + " ms");
      
    } catch (Exception ex) {
      logger.error("TspProvider failed", ex);
    }
  }
}
