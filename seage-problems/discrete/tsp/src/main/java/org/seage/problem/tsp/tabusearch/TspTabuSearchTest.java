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

  public static void main(String[] args) {
    try {
      // String path = "data/tsp/eil51.tsp";//args[0]; // 426
      // String path = "data/tsp/eil101.tsp";//args[0]; // ?
      // String path = "data/tsp/berlin52.tsp";//args[0]; // 7542
      // String path = "data/tsp/ch130.tsp";//args[0]; // 6110
      // String path = "data/tsp/lin318.tsp";//args[0]; // 42029
      // String path = "data/tsp/pcb442.tsp";//args[0]; // 50778
      String path = "data/tsp/u574.tsp";// args[0]; // 36905

      new TspTabuSearchTest().run(path);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    City[] cities = CityProvider.readCities(new FileInputStream(path));
    log.info("Loading cities from path: {}", path);
    log.info("Number of cities: {}", cities.length);

    TabuSearch ts = new TabuSearch(
        // new TspRandomSolution(cities),
        new TspGreedySolution(cities), 
        new TspMoveManager(), 
        new TspObjectiveFunction(cities), 
        new SimpleTabuList(50),
        new BestEverAspirationCriteria(), false);

    ts.addTabuSearchListener(this);
    ts.setIterationsToGo(1500000);
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
