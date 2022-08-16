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

package org.seage.problem.sat.sannealing;

import java.io.FileInputStream;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.sannealing.SimulatedAnnealing;
import org.seage.metaheuristic.sannealing.SimulatedAnnealingEvent;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaReader;
import org.seage.problem.sat.SatPhenotypeEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this class is demonstration of SA algorithm use.
 *
 * @author Richard Malek
 */
public class SatSimulatedAnnealingTest implements IAlgorithmListener<SimulatedAnnealingEvent> {
  private static final Logger log = 
      LoggerFactory.getLogger(SatSimulatedAnnealingTest.class.getName());

  public static void main(String[] args) {
    try {
      // String path = "data/sat/uf20-01.cnf";
      String path = "data/uf100/uf100-01.cnf";

      new SatSimulatedAnnealingTest().run(path);
    } catch (Exception ex) {
      log.error("{}", ex.getMessage(), ex);
    }
  }

  public void run(String path) throws Exception {
    Formula formula = new Formula(new ProblemInstanceInfo("", ProblemInstanceOrigin.FILE, path),
        FormulaReader.readClauses(new FileInputStream(path)));

    SimulatedAnnealing sa = new SimulatedAnnealing(
        new SatObjectiveFunction(new SatPhenotypeEvaluator(null, formula)), new SatMoveManager());

    sa.setMaximalTemperature(200000);
    sa.setMinimalTemperature(0.1);
    sa.setMaximalIterationCount(250000);

    sa.addSimulatedAnnealingListener(this);

    SatSolution solution = new SatSolution(new Boolean[formula.getLiteralCount()]);
    sa.startSearching(solution);

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
