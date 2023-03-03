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

package org.seage.aal.algorithm.tabusearch;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.metaheuristic.tabusearch.BestEverAspirationCriteria;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.metaheuristic.tabusearch.SimpleTabuList;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.metaheuristic.tabusearch.TabuSearch;
import org.seage.metaheuristic.tabusearch.TabuSearchEvent;
import org.seage.metaheuristic.tabusearch.TabuSearchListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TabuSearchAdapter base implementation.
 */
@AlgorithmParameters({ 
    @Parameter(name = "iterationCount", min = 1, max = 100000000000L, init = 100000000000L),
    @Parameter(name = "numSolutions", min = 1, max = 1, init = 1),
    @Parameter(name = "tabuListLength", min = 1, max = 1000, init = 30) })
public abstract class TabuSearchAdapter<P extends Phenotype<?>, S extends Solution> 
    extends AlgorithmAdapterImpl<P, S> {
  
  private static Logger logger = LoggerFactory.getLogger(TabuSearchAdapter.class.getName());
  
  private TabuSearch tabuSearch;
  private TabuSearchObserver observer;
  private int iterationToGo;
  private int tabuListLength;
  protected S[] solutions;
  protected int solutionsToExplore;
  protected S bestEverSolution; // best of all solution
  private AlgorithmParams algParams;

  private double statInitObjVal;
  private double statEndObjVal;
  private int statNumIter;
  private int statNumNewSol;
  private int statLastIterNewSol;

  protected TabuSearchAdapter(MoveManager moveManager, ObjectiveFunction objectiveFunction,
      IPhenotypeEvaluator<P> phenotypeEvaluator) {
    super(phenotypeEvaluator);
    observer = new TabuSearchObserver();
    tabuSearch = new TabuSearch(moveManager, objectiveFunction, false);
    tabuSearch.addTabuSearchListener(observer);
    tabuSearch.setAspirationCriteria(new BestEverAspirationCriteria());
    iterationToGo = 0;
    tabuListLength = 0;
  }

  @Override
  public void startSearching(AlgorithmParams params) throws Exception {
    if (params == null) {
      throw new IllegalArgumentException("Parameters not set");
    }
    setParameters(params);

    reporter = new AlgorithmReporter<>(phenotypeEvaluator);
    reporter.putParameters(params);

    if (this.solutions.length > 1) {
      logger.warn("More than one solutions to solve, used just the first one.");
    }

    statNumNewSol = 0;

    tabuSearch.setCurrentSolution(this.solutions[0]);
    tabuSearch.setTabuList(new SimpleTabuList(tabuListLength));
    tabuSearch.setIterationsToGo(iterationToGo);

    tabuSearch.startSolving();

    // TODO: A - Remove casting
    this.solutions[0] = (S) tabuSearch.getBestSolution();
  }

  @Override
  public void stopSearching() throws Exception {
    tabuSearch.stopSolving();

    while (isRunning()) {
      Thread.sleep(250);
    }
  }

  @Override
  public boolean isRunning() {
    return tabuSearch.isSolving();
  }

  /**
   * Sets the new algorithm parameteres.
   * @param params .
   * @throws Exception .
   */
  public void setParameters(AlgorithmParams params) throws Exception {
    this.algParams = params;
    iterationToGo = statNumIter = algParams.getValueInt("iterationCount");

    tabuListLength = algParams.getValueInt("tabuListLength");
    this.solutionsToExplore = algParams.getValueInt("numSolutions");

  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    reporter.putStatistics(statNumIter, statNumNewSol, statLastIterNewSol, statInitObjVal,
        (statInitObjVal + statEndObjVal) / 2, statEndObjVal);

    return reporter.getReport();
  }

  private class TabuSearchObserver implements TabuSearchListener {

    @Override
    public void tabuSearchStarted(TabuSearchEvent e) {
      algorithmStarted = true;
      statLastIterNewSol = 0;
      logger.debug("TabuSearch started");
    }

    @Override
    public void tabuSearchStopped(TabuSearchEvent e) {
      algorithmStopped = true;
      statEndObjVal = bestEverSolution.getObjectiveValue()[0];
      logger.debug("TabuSearch stopped");
    }

    @Override
    public void newBestSolutionFound(TabuSearchEvent e) {
      try {
        // TODO: A - Remove casting
        S newBest = (S) e.getTabuSearch().getBestSolution();
        bestEverSolution = newBest;

        statLastIterNewSol = e.getTabuSearch().getIterationsCompleted();

        statEndObjVal = bestEverSolution.getObjectiveValue()[0];
        statNumNewSol++;

        if (statNumNewSol == 1) {
          statInitObjVal = statEndObjVal;
        }

        reporter.putNewSolution(
            System.currentTimeMillis(), 
            e.getTabuSearch().getIterationsCompleted(),
            solutionToPhenotype(newBest));
      } catch (Exception ex) {
        logger.error("Failed to report new best solution", ex);
      }

    }

    @Override
    public void newCurrentSolutionFound(TabuSearchEvent e) {
      // Not used yet
    }

    @Override
    public void unimprovingMoveMade(TabuSearchEvent e) {
      // Not used yet
    }

    @Override
    public void improvingMoveMade(TabuSearchEvent e) {
      // Not used yet
    }

    @Override
    public void noChangeInValueMoveMade(TabuSearchEvent e) {
      // Not used yet
    }
  }
}
