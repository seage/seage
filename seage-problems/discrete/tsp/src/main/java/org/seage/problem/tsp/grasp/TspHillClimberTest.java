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
 *     Martin Zaloga
 *     - Initial implementation
 */

package org.seage.problem.tsp.grasp;

import java.io.FileInputStream;
import org.seage.metaheuristic.grasp.HillClimber;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.seage.problem.tsp.TspVisualizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Martin Zaloga
 * @deprecated Replaced by TspProblemSolver
 */
@Deprecated
public class TspHillClimberTest {
  private static final Logger log = LoggerFactory.getLogger(TspHillClimberTest.class.getName());

  /**
   * _cities - List of a loaded cities _tour - Index list of cities that make up
   * the path _hc - Object containing hill climber algorithm
   */
  private City[] cities;
  private Integer[] tour;
  private HillClimber hc;

  /**
   * The main trigger method.
   * 
   * @param args - the argument is the path to the data
   */
  public static void main(String[] args) {
    try {
      new TspHillClimberTest().run(args[0], "greedy", 100000, 10000);
    } catch (Exception ex) {
      log.info(ex.getMessage());
      log.error("{}", ex.getMessage(), ex);
    }
  }

  /**
   * Function for the run the program.
   * 
   * @param path      - Path of data the cities
   * @param switcher  - Switching between the Geedy and Random initial solution
   * @param restarts  - Number of repeat optimalizations algorithm
   * @param iteration - Number of iteration algorthm
   */
  public void run(String path, String switcher, int restarts, int iteration) throws Exception {
    cities = CityProvider.readCities(new FileInputStream(path));
    log.info("Loading cities from path: {}", path);
    log.info("Number of cities: {}", cities.length);

    hc = new HillClimber(new TspObjectiveFunction(cities), new TspMoveManager(),
        new TspSolutionGenerator(switcher, cities), iteration);
    hc.startRestartedSearching(restarts);
    TspSolution bestSol = (TspSolution) hc.getBestSolution();
    tour = bestSol.getTour();

    TspVisualizer.createTourImage(cities, tour, "tsphcgraph.png", 600, 400);
    log.info("length: {}", bestSol.getObjectiveValue());
  }
}
