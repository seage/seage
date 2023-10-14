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

package org.seage.hh.configurator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.seage.aal.problem.ProblemConfig;
import org.seage.aal.problem.ProblemInfo;
import org.seage.data.DataNode;

/**
 * DefaultConfigurator class.
 * @author Richard Malek
 */
public class ExtendedDefaultConfigurator extends Configurator {
  private double spread;

  protected Map<String, Map<String, Map<String, Double>>> bestDefaultParams = Map.of(
      "SAT", Map.of(
          "GeneticAlgorithm", Map.of(
              "crossLengthPct", 2.6714832508502173,
              "eliteSubjectPct", 17.328516749149784,
              "iterationCount", 1000000.0,
              "mutateLengthPct", 2.6714832508502173,
              "mutateSubjectPct", 2.6714832508502173,
              "numSolutions", 172.55231581658285,
              "randomSubjectPct", 17.328516749149784
          ),
          "SimulatedAnnealing", Map.of(
              "iterationCount", 9.342384879287097E8,
              "maxTemperature", 6.58614453755406E7,
              "minTemperature", 0.0,
              "numSolutions", 1.0
          ),
          "AntColony", Map.of(
              "alpha", 2.0288340124934,
              "beta", 2.8417384547786972,
              "iterationCount", 524911.1662639739,
              "evaporationCoef", 0.8273663355790442,
              "numSolutions", 75.0142078836997,
              "quantumOfPheromone", 78.06959520616533
          ),
          "TabuSearch", Map.of(
              "iterationCount", 8.642229643334344E10,
              "numSolutions", 1.0,
              "tabuListLength", 1.0
          )
      ),
      "TSP", Map.of(
          "GeneticAlgorithm", Map.of(
              "crossLengthPct", 0.0,
              "eliteSubjectPct", 0.0,
              "iterationCount", 1000000.0,
              "mutateLengthPct", 0.0,
              "mutateSubjectPct", 0.0,
              "numSolutions", 248.64656805959322,
              "randomSubjectPct", 25.014804854504366
          ),
          "SimulatedAnnealing", Map.of(
            "iterationCount", 9.99999999E8,
            "maxTemperature", 1.644927822941546E8,
            "minTemperature", 0.0,
            "numSolutions", 1.0
          ),
          "AntColony", Map.of(
            "alpha", 2.0288340124934,
            "beta", 2.8417384547786972,
            "iterationCount", 524911.1662639739,
            "evaporationCoef", 0.8273663355790442,
            "numSolutions", 75.0142078836997,
            "quantumOfPheromone", 300.06959520616533
          ),
          "TabuSearch", Map.of(
            "iterationCount", 9.968307133974689E10,
            "numSolutions", 1.0,
            "tabuListLength", 33.16611731596033
          )
       )
  );

  protected Map<String, Map<String, Map<String, Double>>> bestRandomParams = Map.of(
      "SAT", Map.of(
        "GeneticAlgorithm", Map.of(
            "crossLengthPct", 16.92164812580709,
            "eliteSubjectPct", 16.92164812580709,
            "iterationCount", 930790.440390055,
            "mutateLengthPct", 3.0783518741929106,
            "mutateSubjectPct", 16.92164812580709,
            "numSolutions", 168.52431644549017,
            "randomSubjectPct", 3.0783518741929106
        ),
      "SimulatedAnnealing", Map.of(
          "iterationCount", 9.525273894507701E8,
          "maxTemperature", 1000.0,
          "minTemperature", 0.0,
          "numSolutions", 1.0
      ),
      "AntColony", Map.of(
          "alpha", 1.0,
          "beta", 2.859904382201463,
          "iterationCount", 1000000.0,
          "evaporationCoef", 0.9425282337174113,
          "numSolutions", 115.41051795783903,
          "quantumOfPheromone", 1.0
      ),
      "TabuSearch", Map.of(
          "iterationCount", 905467.3922561172,
          "numSolutions", 1.0,
          "tabuListLength", 124.43816957430852
      )
      ),
      "TSP", Map.of(
        "GeneticAlgorithm", Map.of(
            "crossLengthPct", 19.036494354929765,
            "eliteSubjectPct", 19.03649435492976,
            "iterationCount", 1000000.0,
            "mutateLengthPct", 0.9635056450702351,
            "mutateSubjectPct", 0.9635056450702351,
            "numSolutions", 189.46129411380468,
            "randomSubjectPct", 0.9635056450702351
        ),
        "SimulatedAnnealing", Map.of(
            "iterationCount", 9.99999999E8,
            "maxTemperature", 9.74985598511794E7,
            "minTemperature", 0.0,
            "numSolutions", 1.0
        ),
        "AntColony", Map.of(
            "alpha", 1.0,
            "beta", 2.3861673412171998,
            "iterationCount", 931797.0532826431,
            "evaporationCoef", 0.9172622581982506,
            "numSolutions", 167.52159246610802,
            "quantumOfPheromone", 78.13542512489084
        ),
        "TabuSearch", Map.of(
            "iterationCount", 1000000.0,
            "numSolutions", 1.0,
            "tabuListLength", 30.0
        )
    )
  );

  /**
   * DefaultConfigurator constructor.
  */
  public ExtendedDefaultConfigurator() {
    // Default empty constructor.
  }

  @Override
  public ProblemConfig[] prepareConfigs(
      ProblemInfo problemInfo, String instanceID, String algID, int numConfigs)
      throws Exception {

    List<ProblemConfig> results = new ArrayList<>();

    results.add(createConfig(problemInfo, instanceID, algID, 0));
    for (int i = 1; i < numConfigs; i++) {
      double s = Math.random() * this.spread;
      results.add(createConfig(problemInfo, instanceID, algID, s));
    }

    return results.toArray(new ProblemConfig[0]);
  }

  private ProblemConfig createConfig(
      ProblemInfo problemInfo, String instanceID, String algID, double spread)
      throws Exception {
    DataNode problem = new DataNode("Problem");
    problem.putValue("id", problemInfo.getValue("id"));
    problem.putDataNode(problemInfo.getDataNode("Instances").getDataNodeById(instanceID));
    
    DataNode algorithm = new DataNode("Algorithm");
    algorithm.putValue("id", algID);
    
    DataNode params = new DataNode("Parameters");
    ProblemConfig config = new ProblemConfig("Config");
    for (DataNode dn : problemInfo
        .getDataNode("Algorithms").getDataNodeById(algID).getDataNodes("Parameter")) {
      params.putValue(dn.getValueStr("name"), 
          bestDefaultParams.get(problemInfo.getValue("id"))
            .get(algID).get(dn.getValueStr("name")));
    }      

    algorithm.putDataNode(params);

    config.putDataNode(problem);
    config.putDataNode(algorithm);
    return config;
  }

}
