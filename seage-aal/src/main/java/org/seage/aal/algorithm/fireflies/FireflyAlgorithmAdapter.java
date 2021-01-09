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
 * FireflySearchAdapter class
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
  protected List<S> _solutions;
  protected FireflySearch<S> _fireflySearch;
  protected ObjectiveFunction _evaluator;
  protected SolutionComparator _comparator;
  protected FireflySearchObserver _observer;
  protected S _bestEverSolution;
  protected AlgorithmParams _params;

  private double _statInitObjVal;
  private double _statEndObjVal;
  private int _statNumIter;
  private int _statNumNewSol;
  private int _statLastIterNewSol;

  private AlgorithmReporter<P> _reporter;
  private IPhenotypeEvaluator<P> _phenotypeEvaluator;

  // private DataNode _minutes;

  public FireflyAlgorithmAdapter(FireflyOperator operator, ObjectiveFunction evaluator,
      IPhenotypeEvaluator<P> phenotypeEvaluator, boolean maximizing) {
    _evaluator = evaluator;
    _observer = new FireflySearchObserver();
    _comparator = new SolutionComparator();
    _fireflySearch = new FireflySearch<S>(operator, evaluator);
    _fireflySearch.addFireflySearchListener(_observer);
    _phenotypeEvaluator = phenotypeEvaluator;
  }

  /**
   * <running> <parameters/> <minutes/> <statistics/> </running>
   * 
   * @param param
   * @throws java.lang.Exception
   */
  @Override
  public void startSearching(AlgorithmParams params) throws Exception {
    if (params == null)
      throw new Exception("Parameters not set");
    setParameters(params);

    _reporter = new AlgorithmReporter<>(_phenotypeEvaluator);
    _reporter.putParameters(_params);

    _fireflySearch.startSolving(_solutions);
    _solutions = _fireflySearch.getSolutions();
    if (_solutions == null)
      throw new Exception("Solutions null");
  }

  @Override
  public void stopSearching() throws Exception {
    _fireflySearch.stopSolving();

    while (isRunning())
      Thread.sleep(100);
  }

  @Override
  public boolean isRunning() {
    return _fireflySearch.isRunning();
  }

  public void setParameters(AlgorithmParams params) throws Exception {
    _params = params;

    _fireflySearch.setIterationsToGo(_params.getValueInt("iterationCount"));
    _statNumIter = _params.getValueInt("iterationCount");
    _fireflySearch.setAbsorption(_params.getValueDouble("absorption"));
    _fireflySearch.setTimeStep(_params.getValueDouble("timeStep"));
    _fireflySearch
        .setWithDecreasingRandomness(((_params.getValueDouble("withDecreasingRandomness") > 0) ? true : false));
    _fireflySearch.setPopulationCount(_params.getValueInt("numSolutions"));
    // EDD OWN PARAMETERS
  }

  @Override
  public AlgorithmReport getReport() throws Exception {
    int num = _solutions.size();// > 10 ? 10 : solutions.length;
    double avg = 0;
    for (int i = 0; i < num; i++)
      avg += _solutions.get(i).getObjectiveValue()[0];
    avg /= num;

    _reporter.putStatistics(_statNumIter, _statNumNewSol, _statLastIterNewSol, _statInitObjVal, avg, _statEndObjVal);

    return _reporter.getReport();
  }

  private class FireflySearchObserver implements FireflySearchListener<S> {
    @Override
    public void FireflySearchStarted(FireflySearchEvent<S> e) {
      _statNumNewSol = _statLastIterNewSol = 0;
      _algorithmStarted = true;
    }

    @Override
    public void FireflySearchStopped(FireflySearchEvent<S> e) {
      Solution best = e.getFireflySearch().getBestSolution();
      if (best != null)
        _statEndObjVal = best.getObjectiveValue()[0];
      _algorithmStopped = true;
    }

    @Override
    public void newBestSolutionFound(FireflySearchEvent<S> e) {
      try {
        S solution = e.getFireflySearch().getBestSolution();
        _bestEverSolution = solution;
        if (_statNumNewSol == 0)
          _statInitObjVal = solution.getObjectiveValue()[0];

        _reporter.putNewSolution(System.currentTimeMillis(), e.getFireflySearch().getCurrentIteration(),
            solutionToPhenotype(solution));
        _statNumNewSol++;
        _statLastIterNewSol = e.getFireflySearch().getCurrentIteration();

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
