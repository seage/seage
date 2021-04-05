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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     Karel Durkota
 */

package org.seage.aal.algorithm.fireflies;

import java.util.List;

import org.seage.aal.Annotations.AlgorithmParameters;
import org.seage.aal.Annotations.Parameter;
import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReporter;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.FireflySearch;
import org.seage.metaheuristic.fireflies.FireflySearchEvent;
import org.seage.metaheuristic.fireflies.FireflySearchListener;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;
import org.seage.metaheuristic.fireflies.Solution;
import org.seage.metaheuristic.fireflies.SolutionComparator;

/**
 * FireflySearchAdapter base implementation.
 */

@AlgorithmParameters({ @Parameter(name = "iterationCount", min = 500, max = 500, init = 10),
    @Parameter(name = "numSolutions", min = 500, max = 500, init = 500),
    @Parameter(name = "timeStep", min = 0.1, max = 2, init = 0.15),
    @Parameter(name = "withDecreasingRandomness", min = 1, max = 1, init = 1),
    // @Parameter(name="initialIntensity", min=0, max=100000, init=1),
    // @Parameter(name="initialRandomness", min=0, max=100000, init=1),
    // @Parameter(name="finalRandomness", min=0, max=100000, init=0.2),
    @Parameter(name = "absorption", min = 0, max = 1, init = 0.025)
    // @Parameter(name="populationSize", min=0, max=10000, init=0.1)
})
public abstract class FireflyAlgorithmAdapter<P extends Phenotype<?>, S extends Solution>
    extends AlgorithmAdapterImpl<P, S> {
  protected List<S> solutions;
  protected FireflySearch<S> fireflySearch;
  protected ObjectiveFunction evaluator;
  protected SolutionComparator comparator;
  protected FireflySearchObserver observer;
  protected S bestEverSolution;
  protected AlgorithmParams algParams;

  private double statInitObjVal;
  private double statEndObjVal;
  private int statNumIter;
  private int statNumNewSol;
  private int statLastIterNewSol;


  protected FireflyAlgorithmAdapter(
      FireflyOperator operator, ObjectiveFunction evaluator,
      IPhenotypeEvaluator<P> phenotypeEvaluator, boolean maximizing) {
    super(phenotypeEvaluator);
    this.evaluator = evaluator;
    this.observer = new FireflySearchObserver();
    this.comparator = new SolutionComparator();
    this.fireflySearch = new FireflySearch<>(operator, evaluator);
    this.fireflySearch.addFireflySearchListener(observer);
    this.phenotypeEvaluator = phenotypeEvaluator;
  }

  @Override
  public void startSearching(AlgorithmParams params) throws Exception {
    if (params == null) {
      throw new IllegalArgumentException("Parameters not set");
    }
    setParameters(params);

    reporter = new AlgorithmReporter<>(phenotypeEvaluator);
    reporter.putParameters(algParams);

    fireflySearch.startSolving(this.solutions);
    this.solutions = fireflySearch.getSolutions();
    if (this.solutions == null) {
      throw new RuntimeException("Solutions null");
    }
  }

  @Override
  public void stopSearching() throws Exception {
    fireflySearch.stopSolving();

    while (isRunning()) {
      Thread.sleep(100);
    }
  }

  @Override
  public boolean isRunning() {
    return fireflySearch.isRunning();
  }

  /**
   * Sets the new algorithm parameters.
   * @param params .
   * @throws Exception .
   */
  public void setParameters(AlgorithmParams params) throws Exception {
    algParams = params;

    fireflySearch.setIterationsToGo(algParams.getValueInt("iterationCount"));
    statNumIter = algParams.getValueInt("iterationCount");
    fireflySearch.setAbsorption(algParams.getValueDouble("absorption"));
    fireflySearch.setTimeStep(algParams.getValueDouble("timeStep"));
    fireflySearch.setWithDecreasingRandomness(
        ((algParams.getValueDouble("withDecreasingRandomness") > 0) ? true : false));
    fireflySearch.setPopulationCount(algParams.getValueInt("numSolutions"));
    // EDD OWN PARAMETERS
  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    int num = this.solutions.size();// > 10 ? 10 : solutions.length;
    double avg = 0;
    for (int i = 0; i < num; i++) {
      avg += this.solutions.get(i).getObjectiveValue()[0];
    }
    avg /= num;

    reporter.putStatistics(statNumIter, statNumNewSol, statLastIterNewSol, statInitObjVal, avg,
        statEndObjVal);

    return reporter.getReport();
  }

  private class FireflySearchObserver implements FireflySearchListener<S> {
    @Override
    public void FireflySearchStarted(FireflySearchEvent<S> e) {
      statNumNewSol = statLastIterNewSol = 0;
      algorithmStarted = true;
    }

    @Override
    public void FireflySearchStopped(FireflySearchEvent<S> e) {
      Solution best = e.getFireflySearch().getBestSolution();
      if (best != null) {
        statEndObjVal = best.getObjectiveValue()[0];
      }
      algorithmStopped = true;
    }

    @Override
    public void newBestSolutionFound(FireflySearchEvent<S> e) {
      try {
        S solution = e.getFireflySearch().getBestSolution();
        bestEverSolution = solution;
        if (statNumNewSol == 0) {
          statInitObjVal = solution.getObjectiveValue()[0];
        }

        reporter.putNewSolution(
            System.currentTimeMillis(), 
            e.getFireflySearch().getCurrentIteration(),
            solutionToPhenotype(solution));
        statNumNewSol++;
        statLastIterNewSol = e.getFireflySearch().getCurrentIteration();

      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    @SuppressWarnings("unused")
    public void noChangeInValueIterationMade(FireflySearchEvent<S> e) {

    }

    // HAD TO IMPLEMENT BECAUSE OF INTERFACE, MAY CAUSE ALGORITHM NOT TO
    // WORK CORRECTLY
    @Override
    public void newCurrentSolutionFound(FireflySearchEvent<S> e) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void unimprovingMoveMade(FireflySearchEvent<S> e) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void improvingMoveMade(FireflySearchEvent<S> e) {
      throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void noChangeInValueMoveMade(FireflySearchEvent<S> e) {
      throw new UnsupportedOperationException("Not supported yet.");
    }
  }
}
