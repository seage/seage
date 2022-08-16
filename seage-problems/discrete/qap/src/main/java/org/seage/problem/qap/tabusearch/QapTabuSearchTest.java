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
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.qap.tabusearch;

import java.io.FileInputStream;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;
import org.seage.problem.qap.FacilityLocationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author Karel Durkota
 */
public class QapTabuSearchTest implements TabuSearchListener {
  private static final Logger log = LoggerFactory.getLogger(QapTabuSearchTest.class.getName());
  private static String _dataPath = "data/tai12a.dat";

  public static void main(String[] args) {
    try {
      new QapTabuSearchTest().run(_dataPath);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    Double[][][] facilityLocation =
        FacilityLocationProvider.readFacilityLocations(new FileInputStream(path));
    log.info("Loading an instance from path: {}", path);
    log.info("Number of cities: {}", facilityLocation[0].length);

    TabuSearch ts = new TabuSearch(new QapGreedyStartSolution(facilityLocation),
        new QapMoveManager(), new QapObjectiveFunction(facilityLocation), new SimpleTabuList(100),
        new BestEverAspirationCriteria(), false);

    ts.addTabuSearchListener(this);
    ts.setIterationsToGo(1000000);
    ts.startSolving();
    Integer[] r = ((QapSolution) ts.getBestSolution()).getAssign();
    log.info("{}", ts.getBestSolution().getObjectiveValue()[0]);
    String line = "";
    for (int i = 0; i < r.length; i++) {
      line += (r[i] + 1) + ",";
    }
    log.info("{}", line);
  }

  @Override
  public void newBestSolutionFound(TabuSearchEvent e) {
    log.info("{}", e.getTabuSearch().getBestSolution());
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
    // Nothing here
  }

  @Override
  public void unimprovingMoveMade(TabuSearchEvent e) {
    // Nothing here
  }

}
