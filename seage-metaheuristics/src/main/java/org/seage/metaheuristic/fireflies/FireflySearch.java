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
 *     Karel Durkota
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 *
 */

package org.seage.metaheuristic.fireflies;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This version of the {@link FireflySearch} does not create any new threads,
 * making it ideal for embedding in Enterprise JavaBeans. The
 * {@link #startSolving} method blocks until the given number of iterations have
 * been completed.
 * 
 * @author Karel Durkota
 */
public class FireflySearch<S extends Solution> extends FireflySearchBase<S> {

  private static final Logger log = LoggerFactory.getLogger(FireflySearchBase.class.getName());

  private static final long serialVersionUID = -4308076927795750198L;
  private int _iterationCount;
  private int _currentIteration;
  private int _populationCount;

  private boolean _withDecreasingRandomness;
  private boolean _withHillClimbingBestSolution;
  private double _initialIntensity;
  private double _initialRandomness;
  private double _finalRandomness;
  private double _absorption;
  private double _timeStep;

  // private double _randomSubjectPct;
  // private double _mutateSubjectPct;
  // private double _eliteSubjectPct;
  // private double _crossLengthPct;

  private boolean _keepSearching;
  private boolean _isRunning;

  private Population<S> _population;
  private FireflyOperator _operator;
  private ObjectiveFunction _objectiveFunction;

  private S _bestSolution;
  private SolutionComparator _solutionComparator;

  private boolean _maximizing;
  private int _iterationsToGo;
  private boolean _bestSolutionNoMove;

  /**
   * CONSTRUCTOR
   * 
   * @param operator
   * @param objectiveFunction
   */
  public FireflySearch(FireflyOperator operator, ObjectiveFunction objectiveFunction) {
    _operator = operator;
    _objectiveFunction = objectiveFunction;
    _population = new Population<S>();
    new Random();

    _bestSolution = null;
    _solutionComparator = new SolutionComparator();

    _keepSearching = _isRunning = false;
  }

  public FireflyOperator getOperator() {
    return _operator;
  }

  public List<S> getSolutions() throws Exception {
    try {
      return _population.getSolutions(_populationCount);
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
  public void stopSolving() {
    _keepSearching = false;
  }

  @Override
  public boolean isSolving() {
    return _isRunning;
  }

  public S getBestSubject() {
    return _bestSolution;
  }

  private void evaluatePopulation(Population<S> population) throws Exception {
    try {
      for (int i = 0; i < population.getSize(); i++) {
        Solution solution = population.getSolution(i);
        solution.setObjectiveValue(_objectiveFunction.evaluate(solution));
      }
      Collections.sort(population.getList(), _solutionComparator);
    } catch (Exception ex) {
      throw ex;
    }

  }

  @Override
  public void startSolving(List<S> solutions) throws Exception {
    try {
      _isRunning = true;
      _keepSearching = true;
      fireFireflySearchStarted();

      _bestSolution = null;
      _currentIteration = 0;

      Population<S> workPopulation = new Population<>();
      _population.removeAll();
      for (int i = 0; i < _populationCount; i++) {
        // System.out.println(subjects[i].hashCode());
        if (i < solutions.size())
          _population.addSolution(solutions.get(i));
        else
          break;
      }
      double currBestFitness;
      if (_maximizing) {
        currBestFitness = Double.MAX_VALUE;
      } else {
        currBestFitness = Double.MIN_VALUE;
      }
      int i = 0;
      while (i++ < _iterationsToGo && _keepSearching) {
        // System.out.println("---------------------------\n"+i+"-th ITERATION");
        _currentIteration++;

        evaluatePopulation(_population);
        double d = 0;
        for (int s1 = 0; s1 < _population.getSize() - 1; s1++) {
          for (int s2 = s1 + 1; s2 < _population.getSize(); s2++) {
            d = d + _operator.getDistance(_population.getSolutions().get(s1), _population.getSolutions().get(s2));
          }
        }
        // System.out.println("average distance =
        // "+(double)d/(_population.getSize()*(_population.getSize()+1)/2));
        // _population.removeTwins();

        // for(int j=0;j<_population.getSize();j++){
        // System.out.println("\nVypis:"+_population.getSolutions()[j].toString()+" -
        // "+_population.getSolutions()[j].getObjectiveValue()[0]);
        // }

        if (_bestSolution == null || _solutionComparator.compare(_population.getBestSolution(), _bestSolution) < 0) {
          _bestSolution = _population.getBestSolution();
          // System.out.print(i+"\t");
          fireNewBestSolution();
        } else
          ;// fireNoChangeInValueIterationMade();

        // check a worse solution than previous
        currBestFitness = _objectiveFunction.evaluate(_population.getBestSolution())[0];// .getObjectiveValue()[0];
        // System.out.println(currBestFitness);
        if (currBestFitness != _population.getBestSolution().getObjectiveValue()[0]) // SOLVED
          throw new Exception("Fitness function failed");
        // prevFitness = currFitness;

        Collections.shuffle(_population.getList());
        // for(int j=0;j<_population.getSize();j++){
        // System.out.println("\nVypis:"+_population.getSolutions()[j].toString()+" -
        // "+_population.getSolutions()[j].getObjectiveValue()[0]);
        // }
        workPopulation.removeAll();
        workPopulation.mergePopulation(_population);
        for (int s1 = 0; s1 < _population.getSize(); s1++) {
          boolean isBest = true;
          for (int s2 = 0; s2 < _population.getSize(); s2++) {
            if ((_maximizing && workPopulation.getSolution(s1).getObjectiveValue()[0] < workPopulation.getSolution(s2)
                .getObjectiveValue()[0])
                || (!_maximizing && workPopulation.getSolution(s1).getObjectiveValue()[0] > workPopulation
                    .getSolution(s2).getObjectiveValue()[0])) {
              isBest = false;
              _operator.attract(_population.getSolutions().get(s1), workPopulation.getSolutions().get(s2),
                  _currentIteration);

              if (_population.getSolutions().get(s1).equals(workPopulation.getSolutions().get(s2))) {
                // System.out.println("EQUALS\nOLD:"+_population.getSolutions()[s1].toString());
                // _operator.randomSolution(_population.getSolutions()[s1]);
                _operator.modifySolution(_population.getSolutions().get(s1));
                // System.out.println("NEW:"+_population.getSolutions()[s1].toString());
              }
            }
          }
          if (!getBestSolutionNoMove() && isBest == true) {
            _operator.modifySolution(_population.getSolutions().get(s1));
          }
        }
        // hill climbing of best solution
        // double bestEval =
        // _population.getBestSolution().getObjectiveValue()[0];
        if (this.getWithHillClimbingBestSolution()) {
          S best = (S) _population.getBestSolution();
          _operator.modifySolution(best);
          if (_solutionComparator.compare(best, _population.getBestSolution()) < 0)
            _population.addSolution(best);
        }
        log.trace(String.format("%f", _bestSolution.getObjectiveValue()[0]));
      }

      evaluatePopulation(_population);

      fireFireflySearchStopped();
      _isRunning = false;
    } catch (Exception ex) {
      _isRunning = false;
      throw ex;
    }
  }

  @Override
  public void setObjectiveFunction(ObjectiveFunction function) throws Exception {
    _objectiveFunction = function;
  }

  @Override
  public void setBestSolution(S solution) {
    _bestSolution = solution;
  }

  @Override
  public void setIterationsToGo(int iterations) {
    _iterationsToGo = iterations;
  }

  @Override
  public void setMaximizing(boolean maximizing) {
    _maximizing = maximizing;
  }

  public void setPopulationCount(int pop) {
    _populationCount = pop;
  }

  public int getPopulationCount() {
    return _populationCount;
  }

  @Override
  public ObjectiveFunction getObjectiveFunction() {
    return _objectiveFunction;
  }

  @Override
  public S getBestSolution() {
    return _bestSolution;
  }

  @Override
  public int getIterationsToGo() {
    return _iterationsToGo;
  }

  @Override
  public boolean isMaximizing() {
    return _maximizing;
  }

  public double getAbsorption() {
    return _absorption;
  }

  public void setAbsorption(double _absorption) {
    this._absorption = _absorption;
  }

  @Override
  public int getCurrentIteration() {
    return _currentIteration;
  }

  @Override
  public void setCurrentIteration(int _currentIteration) {
    this._currentIteration = _currentIteration;
  }

  public double getFinalRandomness() {
    return _finalRandomness;
  }

  public void setFinalRandomness(double _finalRandomness) {
    this._finalRandomness = _finalRandomness;
  }

  public double getInitialIntensity() {
    return _initialIntensity;
  }

  public void setInitialIntensity(double _initialIntensity) {
    this._initialIntensity = _initialIntensity;
  }

  public double getInitialRandomness() {
    return _initialRandomness;
  }

  public void setInitialRandomness(double _initialRandomness) {
    this._initialRandomness = _initialRandomness;
  }

  public int getIterationCount() {
    return _iterationCount;
  }

  public void setIterationCount(int _iterationCount) {
    this._iterationCount = _iterationCount;
  }

  public double getTimeStep() {
    return _timeStep;
  }

  public void setTimeStep(double _timeStep) {
    this._timeStep = _timeStep;
  }

  public boolean isWithDecreasingRandomness() {
    return _withDecreasingRandomness;
  }

  public void setWithDecreasingRandomness(boolean _withDecreasingRandomness) {
    this._withDecreasingRandomness = _withDecreasingRandomness;
  }

  public void setWithDecreasingRandomness(int _withDecreasingRandomness) {
    if (_withDecreasingRandomness > 0)
      this.setWithDecreasingRandomness(true);
    else
      this.setWithDecreasingRandomness(false);
  }

  public void setWithHillClimbingBestSolution(boolean _withHillClimbingBestSolution) {
    this._withHillClimbingBestSolution = _withHillClimbingBestSolution;
  }

  public boolean getWithHillClimbingBestSolution() {
    return this._withHillClimbingBestSolution;
  }

  public void setBestSolutionNoMove(boolean _bestSolutionNoMove) {
    this._bestSolutionNoMove = _bestSolutionNoMove;
  }

  public boolean getBestSolutionNoMove() {
    return this._bestSolutionNoMove;
  }

  public boolean isRunning() {
    return _isRunning;
  }
}
