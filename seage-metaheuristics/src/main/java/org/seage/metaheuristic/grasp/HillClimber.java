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

package org.seage.metaheuristic.grasp;

/**
 * HillClimber metaheuristic.
 * @author Martin Zaloga
 */
public class HillClimber implements IHillClimber {

  /**
   * numIter - Number of iteration after start Hill-Climber _currentSolution.
   * Actual best solution moveManager - Interface that defines the methods for
   * generating steps objectiveFunction - Interface that defines the methods for
   * selecting steps solutionGenerator - Object for generating solutions
   */
  private int numIter;
  private Solution currentSolution;
  private IMoveManager moveManager;
  private IObjectiveFunction objectiveFunction;
  private ISolutionGenerator solutionGenerator;

  /**
   * Constructor the object Hill-Climber which content the optimization methods.
   * 
   * @param objectiveFunction - Contains methods for selecting steps
   * @param moveManager       - Contains methods for generating steps
   * @param solutionGenerator - Object for generating solutions
   * @param numIter           - Number of iteration
   */
  public HillClimber(IObjectiveFunction objectiveFunction, IMoveManager moveManager,
      ISolutionGenerator solutionGenerator, int numIter) {
    this.moveManager = moveManager;
    this.objectiveFunction = objectiveFunction;
    this.solutionGenerator = solutionGenerator;
    this.numIter = numIter;
  }

  /**
   * Algorithm of Hill-Climber optimalization.
   * 
   * @param solution - Object with an initial solution, which is made better
   */
  @Override
  public void startSearching(Solution solution) throws Exception {
    currentSolution = solution;
    int iter = 0;
    double bestVal = 0;

    while (iter < numIter) {
      IMove[] moves = moveManager.getAllMoves(currentSolution);

      bestVal = solution.getObjectiveValue();

      IMove best = null;
      double val;
      boolean noBetterMove = true;

      /* Browsing generated steps */
      for (IMove m : moves) {
        val = objectiveFunction.evaluateMove(currentSolution, m);

        /* Selection of better solutions */
        if (val < bestVal) {
          best = m;
          bestVal = val;
          noBetterMove = false;
        }
      }
      if (noBetterMove) {
        return;
      }

      /* Selection of the best actual solution */
      if (best != null) {
        currentSolution = best.apply(currentSolution);
        currentSolution.setObjectiveValue(bestVal);
      }

      objectiveFunction.reset();
      iter++;
    }
  }

  /**
   * Restarted algorithm of Hill-Climber optimalization.
   * 
   * @param numRestarts - Number of restarts algorithm
   */
  @Override
  public void startRestartedSearching(int numRestarts) throws Exception {
    int countRest = 0;
    double bestDist = Double.MAX_VALUE;
    Solution bestSolution = null;

    while (countRest <= numRestarts) {

      startSearching(solutionGenerator.generateSolution());
      countRest++;

      /* Choosing the best solution */
      if (bestDist > getBestSolution().getObjectiveValue()) {
        bestDist = getBestSolution().getObjectiveValue();
        bestSolution = getBestSolution();
      }
    }
    if (bestSolution != null) {
      currentSolution = bestSolution;
      currentSolution.setObjectiveValue(bestDist);
    }
  }

  /**
   * Method for setting number of iteration.
   * 
   * @param count - Number of iteration algorithm
   */
  @Override
  public void setIterationCount(int count) {
    numIter = count;
  }

  /**
   * Function which return the best solution.
   * 
   * @return - The best solution
   */
  @Override
  public Solution getBestSolution() {
    return currentSolution;
  }
}
