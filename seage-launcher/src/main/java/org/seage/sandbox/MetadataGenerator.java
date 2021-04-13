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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.aal.problem.ProblemProvider;

import org.seage.data.DataNode;

import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;
import org.seage.problem.sat.SatInitialSolutionProvider;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatPhenotypeEvaluator;
import org.seage.problem.sat.SatProblemProvider;

import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TourProvider;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspPhenotypeEvaluator;
import org.seage.problem.tsp.TspProblemInstance;
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
    ProblemProvider.registerProblemProviders(
        new Class<?>[] {TspProblemProvider.class, SatProblemProvider.class});
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
      new MetadataGenerator().runMetadataGenerator();
      _logger.info("MetadataGenerator finished");
    } catch (Exception ex) {
      _logger.error("MetadataGenerator failed", ex);
    }
  }

  /**
   * Method finds all instances of defines problem and sends it to appropriate method for 
   * further solutions computation.
   * After receiving the results for all problem domains it stores them into a file
   * in xml format.
   */
  public void runMetadataGenerator() throws Exception {
    _logger.info("Radom metadata generator");
    Map<String, IProblemProvider<Phenotype<?>>> providers = ProblemProvider.getProblemProviders();

    for (String problemId : providers.keySet()) {
      _logger.info("Working on " + problemId + " problem...");
      
      IProblemProvider<?> pp = providers.get(problemId);
      ProblemInfo pi = pp.getProblemInfo();
           
      DataNode problem = new DataNode("Problem");
      problem.putValue("id", problemId);

      problem.putDataNode(getInstancesMetadata(pi, 1000));

      saveToFile(problem, problemId.toLowerCase());
    }
  }


  /**
   * Method stores given data into a xml file.
   * @param dn DataNode object with data for outputting.
   */
  private static void saveToFile(DataNode dn, String fileName) throws Exception {
    _logger.info("Saving the results to the file " + fileName + ".metadata.xml");
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
    transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, ""); // Compliant
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource domSource = new DOMSource(dn.toXml());
    StreamResult streamResult = new StreamResult(
        new File("./output/" + fileName + ".metadata.xml"));

    transformer.transform(domSource, streamResult);
  }

  /**
   * Method takes array, finds and returns the median.
   * @param array array of doubles.
   * @return median of given array
   */
  private static double median(double[] array) {
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
   * @param populationCount number of solutions to make.
   * @return DataNode object with random intances metadata
   */
  private DataNode getInstancesMetadata(
      ProblemInfo pi, int populationCount) throws Exception {
    switch (pi.getValueStr("id").toLowerCase()) {
      case "sat": 
        return getSatInstancesMetadata(pi, populationCount);
      case "tsp":
        return getTspInstancesMetadata(pi, populationCount);
      default:
        return null;
    }
  }

  private List<String> getSortedInstanceIDs(ProblemInfo pi) throws Exception {
    List<String> instanceIDs = new ArrayList<>();
    for (DataNode inst : pi.getDataNode("Instances").getDataNodes()) {
      instanceIDs.add(inst.getValueStr("id"));
    }

    Collections.sort(instanceIDs);
    return instanceIDs;
  }

  private static DataNode readOptimalLine(String line) {    
    try (Scanner scanner = new Scanner(line)) {
      scanner.useDelimiter(" : ");

      DataNode result = new DataNode("Optimal");
      result.putValue("name", scanner.next().substring(2).toLowerCase());
      result.putValue("optimum", scanner.next());

      return result;
    }
  }

  private HashMap<String,String> getOptimalValues(String path) throws Exception {
    HashMap<String,String> results = new HashMap<String,String>();

    // Get optimal value
    try (Scanner scanner = new Scanner(getClass().getResourceAsStream(path))) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        if (line.isBlank() || line.isEmpty()) {
          continue;
        }
        if (line.equals("EOF")) {
          break;
        }

        DataNode optimalValue = readOptimalLine(line);

        results.put(
            optimalValue.getValue("name").toString(), 
            optimalValue.getValue("optimum").toString());
      }
    }

    return results;
  }

  /**
   * Method creates for each instance of tsp problem domain number of random solutions.
   * Then it calculates the score of each solution and stores the median to output array.
   * @param populationCount number of random solutions to make.
   */
  private DataNode getTspInstancesMetadata(ProblemInfo pi, int populationCount)
      throws Exception {
    DataNode result = new DataNode("Instances");

    HashMap<String, String> optimumResults = getOptimalValues(
        "/org/seage/problem/tsp/solutions/__optimal.txt");

    //iterate through all instances
    for (String instanceID : getSortedInstanceIDs(pi)) {      
      _logger.info("Processing: " + instanceID);

      String path = String.format("/org/seage/problem/tsp/instances/%s.tsp", instanceID);
      City[] cities = null;

      try (InputStream stream = getClass().getResourceAsStream(path)) {    
        cities = CityProvider.readCities(stream);
      }

      if (cities == null) {
        continue;
      }
      
      double[] randomResults = new double[populationCount];

      ProblemInstanceInfo pii = pi.getProblemInstanceInfo(instanceID);
      TspProblemInstance instance = new TspProblemInstance(pii, cities);
      TspPhenotypeEvaluator tspEval = new TspPhenotypeEvaluator(pi, instance);

      for (int i = 0; i < populationCount; i++) {
        randomResults[i] = tspEval
            .evaluate(new TspPhenotype(
              TourProvider.createGreedyTour(cities, new Random().nextLong())))[0];
      }   
      
      DataNode inst = new DataNode("Instance");
      inst.putValue("id", pi.getDataNode("Instances").getDataNodeById(instanceID).getValue("id"));
      inst.putValue("random", (int)median(randomResults));

      if (optimumResults.containsKey(instanceID.toLowerCase())) {
        inst.putValue("optimum", optimumResults.get(instanceID.toLowerCase()));;
      } else {
        inst.putValue("optimum", "TBA");
      }

      inst.putValue("size", cities.length);
      result.putDataNode(inst);      
    } 
    return result;
  }

  /**
   * Method creates for each instance of sat problem domain number of random solutions.
   * Then it calculates the score of each solution and stores the median to output array.
   * @param populationCount number of random solutions to make.
   */
  private DataNode getSatInstancesMetadata(ProblemInfo pi, int populationCount)
       throws Exception {
    DataNode result = new DataNode("Instances");
    //iterate through all instances
    for (String instanceID : getSortedInstanceIDs(pi)) {  
      _logger.info("Processing: " + instanceID);

      String path = String.format("/org/seage/problem/sat/instances/%s.cnf", instanceID);

      Formula formula = null;
      
      try (InputStream stream = getClass().getResourceAsStream(path)) {
        formula = new Formula(new ProblemInstanceInfo("", ProblemInstanceOrigin.FILE, path),
        FormulaReader.readClauses(stream));
      }
      
      double[] randomResults = new double[populationCount];

      SatProblemProvider provider = new SatProblemProvider();
      ProblemInstance problemInstance = provider
          .initProblemInstance(pi.getProblemInstanceInfo(instanceID));
      IPhenotypeEvaluator<SatPhenotype> evaluator = provider
          .initPhenotypeEvaluator(problemInstance);

      SatPhenotypeEvaluator satEval = new SatPhenotypeEvaluator(pi, formula);

      for (int i = 0; i < populationCount; i++) {
        randomResults[i] = satEval.evaluate(SatInitialSolutionProvider
          .generateGreedySolution(formula, evaluator, new Random().nextLong()))[0];
      }

      DataNode inst = new DataNode("Instance");
      inst.putValue("id", pi.getDataNode("Instances").getDataNodeById(instanceID).getValue("id"));
      inst.putValue("random", (int)median(randomResults));
      inst.putValue("optimum", 0);
      inst.putValue("size", formula.getLiteralCount());
      result.putDataNode(inst);      
    }
    return result;
  }
}
