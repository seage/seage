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
 * Contributors: Jan Zmatlik - Initial implementation
 */

package org.seage.problem.qap.sannealing;

import java.io.FileInputStream;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.problem.qap.FacilityLocationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Karel Durkota
 */
public class QapSimulatedAnnealingTest implements IAlgorithmListener<SimulatedAnnealingEvent> {
  private static final Logger log = 
      LoggerFactory.getLogger(QapSimulatedAnnealingTest.class.getName());

  private Double[][][] facilityLocation;
  private static String dataPath = "data/tai12a.dat";

  public static void main(String[] args) {
    try {
      new QapSimulatedAnnealingTest().run(dataPath);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    facilityLocation = FacilityLocationProvider.readFacilityLocations(new FileInputStream(path));
    log.info("Loading Facilities & Locations from path: {}", path);
    log.info("Number of facilities and locations: {}", facilityLocation.length);

    SimulatedAnnealing sa =
        new SimulatedAnnealing(new QapObjectiveFunction(), new QapMoveManager());

    sa.setMaximalTemperature(2000);
    sa.setMinimalTemperature(0.1);
    // sa.setAnnealingCoefficient( 0.99 );
    sa.setMaximalIterationCount(2500);
    // sa.setMaximalAcceptedSolutionsPerOneStepCount(100);

    sa.addSimulatedAnnealingListener(this);
    sa.startSearching(new QapGreedySolution(facilityLocation));

    log.info("{}", sa.getBestSolution());
    String line = "";
    for (int i = 0; i < ((QapSolution) sa.getBestSolution())._assign.length; i++) {
      line += ((QapSolution) sa.getBestSolution())._assign[i] + ", ";
    }
    log.info("{}", line);
    log.info("\nEVAL: {}", ((QapSolution) sa.getBestSolution()).getObjectiveValue());
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
  public void newBestSolutionFound(SimulatedAnnealingEvent e) {
    log.info("Best: {}", e.getSimulatedAnnealing().getBestSolution().getObjectiveValue());
  }

  @Override
  public void iterationPerformed(SimulatedAnnealingEvent e) {
    // Nothing here
  }

  @Override
  public void noChangeInValueIterationMade(SimulatedAnnealingEvent e) {
    // Nothing here
  }

}
