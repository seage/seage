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

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemProvider;

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
import org.seage.problem.tsp.TspProblemProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This program creates number of random solutions for each given instance of a problem domain.
 * And for every instance returns median of obtained solutions.
 * 
 * @author David Omrai
 */

public class MetadataGenerator {
  static {
    ProblemProvider.providerClasses =
        new Class<?>[] {TspProblemProvider.class, SatProblemProvider.class};
  }

  private static final Logger _logger = LoggerFactory.getLogger(MetadataGenerator.class.getName());


  /**
   * Main method of MetadataGenerator class.
   * In this method creates array of instances for each 
   * problem domain.
   * The instances are then given to run method.
   * @param args input parameters.
   */
  public static void main(String[] args) {
    try {     
      _logger.info("MetadataGenerator is running...");
      new MetadataGenerator().runRandomMetadataGenerator();
      _logger.info("MetadataGenerator finished");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Run method takes given instances and sends it to appropriate method for 
   * further computation.
   * After receiving the results for all problem domains it stores them into a file
   * in xml format.
   */
  public void runRandomMetadataGenerator() throws Exception {
    
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();

    for (String problemId : providers.keySet()) {
      _logger.info("Working on " + problemId + " problem...");
      
      IProblemProvider<?> pp = providers.get(problemId);
      DataNode pi = pp.getProblemInfo();
      
      problemsRandomMetaGenetatorHandler(
          problemId, 1000, pi.getDataNode("Instances").getDataNodes());

      if (problemId == null) {
        continue;
      }

      DataNode results = new DataNode("results");
      DataNode problem = new DataNode(problemId + "-problem");

      for (DataNode dn: pi.getDataNode("Instances").getDataNodes()) {
        DataNode inst = new DataNode("instance");
        inst.putValue(
            "name",
            dn.getValue("id").toString());
        
        inst.putValue(
            "random",
            dn.getValue("random").toString());

        inst.putValue(
            "size",
            dn.getValue("size").toString());

        problem.putDataNode(inst);
      }
      results.putDataNode(problem);

      DataNode mdGenRes = new DataNode("MetadataGenerator");
      mdGenRes.putDataNode(results);

      saveToFile(mdGenRes, problemId.toLowerCase());
    }
  }

  public void runOptimalMetadataGenerator(){
    //todo
  }

  /**
   * Method stores given data into a xml file.
   * @param dn DataNode object with data for outputting.
   */
  public static void saveToFile(DataNode dn, String fileName) throws Exception {
    _logger.info("Saving the results to the file " + fileName + ".metadata.xml...");
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    DOMSource domSource = new DOMSource(dn.toXml());
    StreamResult streamResult = new StreamResult(new File("./" + fileName + ".metadata.xml"));

    transformer.transform(domSource, streamResult);
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
    return ((double)array[ (array.length / 2)]);
  }

  /**
   * Method decides what metaGenerator to call deppending on given problem id.
   * @param populationCount number of random solutions to make.
   * @param instancesIds array with instances ids.
   * @return 
   */
  public void problemsRandomMetaGenetatorHandler(
        String problemId, int populationCount,  List<DataNode> instancesIds) throws Exception {
    switch (problemId.toLowerCase()) {
      case "sat": 
        satRandomMetadataGenerator(populationCount, instancesIds);
        break;
      case "tsp":
        tspRandomMetadataGenerator(populationCount, instancesIds);
        break;
      default:
        break;
    }
  }

  /**
   * Method creates for each instance of tsp problem domain number of random solutions.
   * Then it calculates the score of each solution and stores the median to output array.
   * @param populationCount number of random solutions to make.
   * @param instancesIds array with instances ids.
   */
  public void tspRandomMetadataGenerator(int populationCount,  List<DataNode> instancesIds)
      throws Exception {
  
    //iterate through all instances
    for (int ins = 0; ins < instancesIds.size(); ins++) {
      _logger.info("Calculating: " + instancesIds.get(ins).getValue("id"));

      String path = String.format("/org/seage/problem/tsp/instances/%s.tsp", 
          instancesIds.get(ins).getValue("id"));
      City[] cities = null;

      try (InputStream stream = getClass().getResourceAsStream(path)) {    
        cities = CityProvider.readCities(stream);
      }

      if (cities == null) {
        continue;
      }
      
      double[] randomResults = new double[populationCount];

      TspPhenotypeEvaluator tspEval = new TspPhenotypeEvaluator(cities);

      for (int i = 0; i < populationCount; i++) {
        randomResults[i] = tspEval
        .evaluate(new TspPhenotype(TourProvider.createRandomTour(cities.length)))[0];
      }

      instancesIds.get(ins).putValue("random", median(randomResults));
      instancesIds.get(ins).putValue("size", cities.length);
      
    } 
  }

  /**
   * Method creates for each instance of sat problem domain number of random solutions.
   * Then it calculates the score of each solution and stores the median to output array.
   * @param populationCount number of random solutions to make.
   * @param instancesIds array with instances ids.
   */
  public void satRandomMetadataGenerator(int populationCount,  List<DataNode> instancesIds)
       throws Exception {
  
    //iterate through all instances
    for (int ins = 0; ins < instancesIds.size(); ins++) {
      _logger.info("Calculating: " + instancesIds.get(ins).getValue("id"));

      String path = String.format("/org/seage/problem/sat/instances/%s.cnf", 
          instancesIds.get(ins).getValue("id"));

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

      instancesIds.get(ins).putValue("random", median(randomResults));
      instancesIds.get(ins).putValue("size", formula.getLiteralCount());
    }
  }
}
