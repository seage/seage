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

import java.io.InputStream;
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

  /**. */
  public static void main(String[] args) {
    try {

      // String instanceID = "eil51"; // 426
      // String instanceID = "berlin52"; // 7542
      // String instanceID = "ch130"; // 6110
      // String instanceID = "lin318"; // 42029
      // String instanceID = "pcb442"; // 50778
      // String instanceID = "u574"; // 36905
      String instanceID = "u724-hyflex-3"; // 41910
      // String instanceID = "rat575-hyflex-2"; // 6773 
      // String instanceID = "d1291-hyflex-6"; // 50801 
      // String instanceID = "pr299-hyflex-0"; // 48191 
      // String instanceID = "u2152-hyflex-7"; // 64253
      // String instanceID = "usa13509-hyflex-8"; // 19982859
      new TspSimulatedAnnealingTest().run(instanceID);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  /**. */
  public void run(String instanceID) throws Exception {
    String path = String.format("/org/seage/problem/tsp/instances/%s.tsp", instanceID);
    City[] cities = null;
    try (InputStream stream = getClass().getResourceAsStream(path)) {
      cities = CityProvider.readCities(stream);
    }
    log.info("Loading cities from path: {}", path);
    log.info("Number of cities: {}", cities.length);

    TspObjectiveFunction objFunction = new TspObjectiveFunction(cities);
    SimulatedAnnealing<TspSolution> sa = 
        new SimulatedAnnealing<>(objFunction, new TspMoveManager());

    sa.setMaximalTemperature(10000000d);
    sa.setMinimalTemperature(0.0d);
    sa.setAnnealingCoefficient(0.999995);
    sa.setMaximalIterationCount(10_000_000);

    sa.addSimulatedAnnealingListener(this);
    // TspGreedySolution s = new TspGreedySolution(cities);
    TspRandomSolution s = new TspRandomSolution(cities.length);

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
