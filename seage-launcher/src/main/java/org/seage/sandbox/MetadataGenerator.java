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

package org.seage.sandbox;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;

import org.seage.data.DataNode;

import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;
import org.seage.problem.sat.SatPhenotypeEvaluator;
import org.seage.problem.sat.SatProblemProvider;

import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TourProvider;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspPhenotypeEvaluator;



/**
 * This program creates number of random solutions for each given instance of a problem domain.
 * And for every instance returns median of obtained solutions.
 * 
 * @author David Omrai
 */

public class MetadataGenerator {
  
  /**
   * Main method of MetadataGenerator class.
   * In this method creates array of instances for each 
   * problem domain.
   * The instances are then given to run method.
   * @param args input parameters.
   */
  public static void main(String[] args) {
    try {
      String[] tspInstancesID = new String[]{
        "hyflex-tsp-0",
        "hyflex-tsp-8",
        "hyflex-tsp-2",
        "hyflex-tsp-7",
        "hyflex-tsp-6"
      };
      String[] satInstancesID = new String[]{
        "hyflex-sat-3",
        "hyflex-sat-5",
        "hyflex-sat-4",
        "hyflex-sat-10",
        "hyflex-sat-11"
      };

      new MetadataGenerator().run(tspInstancesID, satInstancesID);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Run method takes given instances and sends it to appropriate method for 
   * further computation.
   * After receiving the results for all problem domains it stores them into a file
   * in xml format.
   * @param tspInstancesID instances ids of tsp problem domain. 
   * @param satInstancesID instances ids of sat problem domain.
   */
  public void run(String[] tspInstancesID, String[] satInstancesID) throws Exception {
    double[] tspMedianResults = tspMetaGenerator(10000, tspInstancesID);
    double[] satMedianResults = satMetaGenerator(10000, satInstancesID);

    DataNode results = new DataNode("results");

    DataNode tspRes = new DataNode("tsp-problem");
    for (int i = 0; i < tspInstancesID.length; i++) {
      DataNode inst = new DataNode("instance");
      inst.putValue(tspInstancesID[i], Double.toString(tspMedianResults[i]));

      tspRes.putDataNode(inst);
    }
    results.putDataNode(tspRes);

    DataNode satRes = new DataNode("sat-problem");
    for (int i = 0; i < satInstancesID.length; i++) {
      DataNode inst = new DataNode("instance");
      inst.putValue(satInstancesID[i], Double.toString(satMedianResults[i]));

      satRes.putDataNode(inst);
    }
    results.putDataNode(satRes);


    DataNode mdGenRes = new DataNode("MetadataGenerator");
    mdGenRes.putDataNode(results);
    System.out.println(mdGenRes.getDataNodes());
    System.out.println(mdGenRes.toString());
  }


  /**
   * Method takes array, finds and returns the median.
   * @param array array of doubles.
   * @return median of given array
   */
  public static double median(double[] array) {
    if (array.length == 1) {
      return array[0];
    }

    Arrays.sort(array);
    if (array.length % 2 == 0) {
      return (((double)array[array.length / 2] + (double)array[array.length / 2 - 1]) / 2);
    }
    return ((double)array[ (array.length / 2) + 1 ]);
  }

  /**
   * Method creates for each instance of tsp problem domain number of random solutions.
   * Then it calculates the score of each solution and stores the median to output array.
   * @param populationCount number of random solutions to make.
   * @param instancesID array with instances ids.
   * @return array of medians for each instance
   */
  public double[] tspMetaGenerator(int populationCount, String[] instancesID) throws Exception {
    double[] results = new double[instancesID.length];
  
    //iterate through all instances
    for (int ins = 0; ins < instancesID.length; ins++) {
      String path = String.format("/org/seage/problem/tsp/instances/%s.tsp", instancesID[ins]);
      City[] cities = null;

      try (InputStream stream = getClass().getResourceAsStream(path)) {    
        cities = CityProvider.readCities(stream);
      }
      
      double[] randomResults = new double[populationCount];

      TspPhenotypeEvaluator tspEval = new TspPhenotypeEvaluator(cities);

      for (int i = 0; i < populationCount; i++) {
        randomResults[i] = tspEval
        .evaluate(new TspPhenotype(TourProvider.createRandomTour(cities.length)))[0];
      }

      results[ins] = median(randomResults);
    } 
    return results;
  }

  /**
   * Method creates for each instance of sat problem domain number of random solutions.
   * Then it calculates the score of each solution and stores the median to output array.
   * @param populationCount number of random solutions to make.
   * @param instancesID array with instances ids.
   * @return array of medians for each instance
   */
  public double[] satMetaGenerator(int populationCount, String[] instancesID)
       throws Exception {
    double[] results = new double[instancesID.length];
  
    //iterate through all instances
    for (int ins = 0; ins < instancesID.length; ins++) {
      String path = String.format("/org/seage/problem/sat/instances/%s.cnf", instancesID[ins]);

      Formula formula = null;
      
      try (InputStream stream = getClass().getResourceAsStream(path)) {
        formula = new Formula(new ProblemInstanceInfo("", ProblemInstanceOrigin.FILE, path),
        FormulaReader.readClauses(stream));
      }

      double[] randomResults = new double[populationCount];

      SatPhenotypeEvaluator satEval = new SatPhenotypeEvaluator(formula);

      for (int i = 0; i < populationCount; i++) {
        randomResults[i] = satEval.evaluate(
          new SatProblemProvider()
          .generateInitialSolutions((ProblemInstance)formula, 1, new Random().nextLong())[0])[0];
      }

      results[ins] = median(randomResults);
    } 
    return results;
  }
}
