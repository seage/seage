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
 *     Jan Zmatlik
 *     - Initial implementation
 */

package org.seage.problem.tsp.sannealing;

import java.io.FileInputStream;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.ISimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Jan Zmatlik
 */
public class TspSimulatedAnnealingTest implements IAlgorithmListener<SimulatedAnnealingEvent> {
  private static final Logger log = 
      LoggerFactory.getLogger(TspSimulatedAnnealingTest.class.getName());
  private City[] cities;
  // private static String _dataPath = "D:\\eil51.tsp";

  public static void main(String[] args) {
    try {

      String path = "data/tsp/eil51.tsp";// args[0]; // 426
      // String path = "data/tsp/berlin52.tsp";//args[0]; // 7542
      // String path = "data/tsp/ch130.tsp";//args[0]; // 6110
      // String path = "data/tsp/lin318.tsp";//args[0]; // 42029
      // String path = "data/tsp/pcb442.tsp";//args[0]; // 50778
      // String path = "data/tsp/u574.tsp";//args[0]; // 36905

      new TspSimulatedAnnealingTest().run(path);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    cities = CityProvider.readCities(new FileInputStream(path));
    log.info("Loading cities from path: {}", path);
    log.info("Number of cities: {}", cities.length);

    TspObjectiveFunction objFunction = new TspObjectiveFunction(cities);
    SimulatedAnnealing sa = new SimulatedAnnealing(objFunction, new TspMoveManager2(objFunction));

    sa.setMaximalTemperature(1000000.0d);
    sa.setMinimalTemperature(0.01d);
    // sa.setAnnealingCoefficient( 0.9999999);
    sa.setMaximalIterationCount(100000000);

    sa.addSimulatedAnnealingListener(this);
    TspGreedySolution s = new TspGreedySolution(cities);
    // TspRandomSolution s = new TspRandomSolution(_cities.length);

    log.info("{}", objFunction.getObjectiveValue(s));
    sa.startSearching(s);

    log.info("{}", sa.getBestSolution().getObjectiveValue());
    log.info("{}", sa.getBestSolution());
  }

  @Override
  public void algorithmStarted(SimulatedAnnealingEvent e) {
    log.info("Started");
  }

  @Override
  public void algorithmStopped(SimulatedAnnealingEvent e) {
    log.info("Stopped");
  }

  @Override
  public void iterationPerformed(SimulatedAnnealingEvent e) {
    // Nothing here
  }

  @Override
  public void noChangeInValueIterationMade(SimulatedAnnealingEvent e) {
    // Nothing here
  }

  @Override
  public void newBestSolutionFound(SimulatedAnnealingEvent e) {
    ISimulatedAnnealing<?> sa = e.getSimulatedAnnealing();
    log.info("New best: {} - iter: {} - temp: {}", 
        sa.getBestSolution().getObjectiveValue(),
        sa.getCurrentIteration(), sa.getCurrentTemperature());
  }

}
