/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.qap;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Karel Durkota
 */
public class AssignmentProvider {
  private static final Logger log = LoggerFactory.getLogger(AssignmentProvider.class.getName());

  public static void main(String[] args) {
    try {
      // String instanceName = "tai12a";
      // String instanceName = "tai25b";
      String instanceName = "sko100a";
      // String instanceName = "tai150b";
      // String instanceName = "tho150";
      // String instanceName = "tai256c";
      String instancePath =
          "seage-problems/discrete/qap/src/main/resources/org/seage/problem/qap/instances/%s.dat"
              .formatted(instanceName);

      long t0 = System.currentTimeMillis();

      log.info("Instance: {}", instancePath);

      Double[][][] facilityLocation =
          FacilityLocationProvider.readFacilityLocations(new FileInputStream(instancePath));

      // Integer[] optimal = new Integer[] {8, 1, 6, 2, 11, 10, 3, 5, 9, 7, 12, 4};
      // optimal = Arrays.stream(optimal).map(e -> e - 1).toArray(Integer[]::new);
      // double optimalValue = getAssignmentPrice(optimal, facilityLocation);
      // log.info("Optimal value: " + optimalValue);

      log.info("Facilities & Locations: {}", facilityLocation.length);
      log.info("{}");

      log.info("Read: {} ms", (System.currentTimeMillis() - t0));
      t0 = System.currentTimeMillis();

      for (int i = 0; i < 1; i++) {
        Integer[] greedyAssignment = createGreedyAssignment(facilityLocation);
        // log.info("Creation: " + (System.currentTimeMillis() - t0) / 1000 + "s");
        // t0 = System.currentTimeMillis();

        double greedyTotalPrice = getAssignmentPrice(greedyAssignment, facilityLocation);
        // log.info("Evaluation: " + (System.currentTimeMillis() - t0) + " ms");
        // t0 = System.currentTimeMillis();
        // log.info();
        log.info("Greedy assignment total price: {}", (int)greedyTotalPrice);
      }

      log.info("{}");

      for (int i = 0; i < 10; i++) {
        Integer[] randomAssignment = createRandomAssignment(facilityLocation);
        // log.info("Creation: " + (System.currentTimeMillis() - t0) / 1000 + "
        // s");
        // t0 = System.currentTimeMillis();

        double randomTotalPrice = getAssignmentPrice(randomAssignment, facilityLocation);
        // log.info("Evaluation: " + (System.currentTimeMillis() - t0) + " ms");
        // t0 = System.currentTimeMillis();
        // log.info();
        log.info("Random assignment total price: {}", (int)randomTotalPrice);
      }

      log.info("Time: {} s", (System.currentTimeMillis() - t0) / 1000);

    } catch (

    Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public static Integer[] createGreedyAssignment(Double[][][] facilityLocation) throws Exception {

    double bestSoFarPrice = Double.MAX_VALUE;
    Integer[] bestAssignment = new Integer[facilityLocation[0].length];
    Random rnd = new Random(1);
    for (int k = 0; k < bestAssignment.length; k++) {
      // for (int k = 0; k < 1; k++) {
      Integer[] assignment = new Integer[facilityLocation[0].length];
      boolean[] avail = new boolean[facilityLocation[0].length];
      for (int i = 0; i < avail.length; i++) {
        assignment[i] = -1;
        avail[i] = true;
      }
      assignment[0] = k;// rnd.nextInt(assignment.length);
      avail[assignment[0]] = false;

      for (int i = 1; i < assignment.length; i++) {
        int location = -1;
        double bestPrice = Double.MAX_VALUE;
        // log.info("----------");
        // log.info("Step " + i);
        for (int j = 0; j < avail.length; j++) {
          if (!avail[j]) {
            continue;
          }
          double newPrice = getFacilityPrice(assignment, i, j, facilityLocation);
          if (newPrice < bestPrice && newPrice != 0) {
            bestPrice = newPrice;
            location = j;
          }
          // log.info(newPrice);
        }
        // log.info("->" + bestPrice);
        if (location == -1) {
          int ix = rnd.nextInt(avail.length);
          while (!avail[++ix % avail.length]) {
            ;
          }
          location = ix % avail.length;
        }
        assignment[i] = location;
        avail[location] = false;
      }
      double currPrice = getAssignmentPrice(assignment, facilityLocation);
      if (bestSoFarPrice > currPrice) {
        bestSoFarPrice = currPrice;
        bestAssignment = assignment;
      }
    }
    return bestAssignment;
  }

  public static double[] sumOfRows(Double[][] matrix) {
    double[] res = new double[matrix.length];
    for (int i = 0; i < matrix.length; i++) {
      res[i] = addArray(matrix[i]);
    }
    return res;
  }

  public static double addArray(Double[] row) {
    double res = 0;
    for (int i = 0; i < row.length; i++) {
      res += row[i];
    }
    return res;
  }

  // TODO: A - create better implementation
  public static Integer[] createRandomAssignment(Double[][][] facilityLocation) {
    // Integer[] assign = new Integer[ facilityLocation[0][0].length ];
    List<Integer> listAssign = new ArrayList<Integer>();
    for (int i = 0; i < facilityLocation[0][0].length; i++) {
      listAssign.add(i);
    }
    Collections.shuffle(listAssign);
    return listAssign.toArray(new Integer[0]);
  }

  public static Integer[] createSortedAssignment(Double[][][] facilityLocation) {
    Integer[] assignment = new Integer[facilityLocation[0].length];
    for (int i = 0; i < assignment.length; i++) {
      assignment[i] = i;
    }

    return assignment;
  }

  public static double getAssignmentPrice(Integer[] assignment, Double[][][] facilityLocation)
      throws Exception {
    double price = 0;
    for (int i = 0; i < facilityLocation[0][0].length; i++) {
      int facilityA = i;
      int locationA = assignment[i];
      for (int j = 0; j < facilityLocation[1][0].length; j++) {
        int facilityB = j;
        int locationB = assignment[j];
        // price += flow(A, B) * priceForDistance(A, B);
        price +=
            facilityLocation[0][facilityA][facilityB] * facilityLocation[1][locationA][locationB];
      }
    }
    return price;
  }

  public static double getFacilityPrice(Integer[] assignment, int facility, int location,
      Double[][][] facilityLocation) throws Exception {
    double price = 0;
    for (int j = 0; j < assignment.length; j++) {
      int facilityB = j;
      int locationB = assignment[j];
      if (locationB == -1) {
        break;
      }
      // price += flow(A, B) * priceForDistance(A, B);
      price += facilityLocation[0][facility][facilityB] * facilityLocation[1][location][locationB];
    }

    return price;
  }
}
