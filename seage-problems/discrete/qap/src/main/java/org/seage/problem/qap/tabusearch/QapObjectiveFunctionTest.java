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
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;
import org.seage.problem.qap.FacilityLocationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * 
 * @author Karel Durkota
 */
public class QapObjectiveFunctionTest implements TabuSearchListener {
  private static final Logger log =
      LoggerFactory.getLogger(QapObjectiveFunctionTest.class.getName());
  // private static String _dataPath = "data/tai12a.dat";
  private static String _dataPath = "D:\\qap\\bur26a.dat";
  // private static Integer[] assign = {0,1,5,3,7,2,6,4,13,12,15,14,8,10,9,11};

  private static Integer[] assign = {1, 3, 2, 4, 6, 8, 7, 8, 6, 5, 14, 13, 16, 15, 9, 11, 10, 12};

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

    QapObjectiveFunction qof = new QapObjectiveFunction(facilityLocation);
    log.info("{}", qof.evaluate(new QapSolution(assign), null)[0]);
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
