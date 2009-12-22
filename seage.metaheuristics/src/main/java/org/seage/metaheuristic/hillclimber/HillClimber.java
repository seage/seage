/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.metaheuristic.hillclimber;

/**
 *
 * @author Martin Zaloga
 */
public class HillClimber implements IHillClimber {

    private int _numIter;
    private Solution _currentSolution;
    private IMoveManager _moveManager;
    private IObjectiveFunction _objectiveFunction;

    public HillClimber(IObjectiveFunction objectiveFunction, IMoveManager moveManager) {
        _moveManager = moveManager;
        _objectiveFunction = objectiveFunction;
    }

    public void startSearching(Solution solution) {
       _currentSolution = solution;

        int iter = 0;
        double bestVal = _objectiveFunction.evaluateMove(_currentSolution, null);
        while (iter < _numIter) {
            IMove[] moves = _moveManager.getAllMoves(_currentSolution);

            IMove best = null;
            for (IMove m : moves) {
                double val = _objectiveFunction.evaluateMove(_currentSolution, m);
                if (val < bestVal) {
                    best = m;
                    bestVal = val;
                }
            }

            if (best != null) {
                //System.out.println(""+bestVal);
                _currentSolution = best.apply(_currentSolution);
                _currentSolution.setObjectiveValue(bestVal);
            }
            iter++;
        }
    }

    public void setIterationCount(int count) {
        _numIter = count;
    }

    public Solution getBestSolution() {
        return _currentSolution;
    }
}
