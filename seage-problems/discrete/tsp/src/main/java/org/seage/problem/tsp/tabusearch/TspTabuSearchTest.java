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
package org.seage.problem.tsp.tabusearch;

import java.io.FileInputStream;
import java.io.InputStream;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.CityProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Richard Malek
 */
public class TspTabuSearchTest implements TabuSearchListener {
  private static final Logger log = LoggerFactory.getLogger(TspTabuSearchTest.class.getName());

  private static final int TABU_LIST_SIZE = 200;
  private static final int ITERATIONS = 500000;

  public static void main(String[] args) {
    // String instanceID = "eil51"; // 426
    // String instanceID = "berlin52"; // 7542
    // String instanceID = "ch130"; // 6110
    // String instanceID = "lin318"; // 42029
    // String instanceID = "pcb442"; // 50778
    // String instanceID = "u574"; // 36905
    // String instanceID = "u724-hyflex-3"; // 41910
    // String instanceID = "rat575-hyflex-2"; // 6773 
    // String instanceID = "d1291-hyflex-6"; // 50801 
    // String instanceID = "pr299-hyflex-0"; // 48191 
    // String instanceID = "u2152-hyflex-7"; // 64253
    String instanceID = "usa13509-hyflex-8"; // 19982859    
    try {
      new TspTabuSearchTest().run(instanceID);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String instanceID) throws Exception {
    String path = String.format("/org/seage/problem/tsp/instances/%s.tsp", instanceID);
    City[] cities = null;
    try (InputStream stream = getClass().getResourceAsStream(path)) {
      cities = CityProvider.readCities(stream);
    }
    log.info("Loading cities from path: {}", path);
    log.info("Number of cities: {}", cities.length);

    TabuSearch ts = new TabuSearch(
        // new TspRandomSolution(cities),
        new TspGreedySolution(cities), 
        new TspMoveManager(), 
        new TspObjectiveFunction(cities), 
        new SimpleTabuList(TABU_LIST_SIZE),
        new BestEverAspirationCriteria(), false);

    ts.addTabuSearchListener(this);
    ts.setIterationsToGo(ITERATIONS);
    ts.startSolving();
  }

  @Override
  public void newBestSolutionFound(TabuSearchEvent e) {
    log.info("{} - {}", 
        e.getTabuSearch().getBestSolution(), 
        e.getTabuSearch().getIterationsCompleted());
  }

  @Override
  public void improvingMoveMade(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void newCurrentSolutionFound(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void noChangeInValueMoveMade(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void tabuSearchStarted(TabuSearchEvent e) {
    // Nothing here
  }

  @Override
  public void tabuSearchStopped(TabuSearchEvent e) {
    log.info("finished");
  }

  @Override
  public void unimprovingMoveMade(TabuSearchEvent e) {
    // Nothing here
  }

}
