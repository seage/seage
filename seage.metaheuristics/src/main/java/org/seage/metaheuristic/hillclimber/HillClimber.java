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

    /**
     * _numIter - Number of iteration after start Hill-Climber
     * _currentSolution - Actual best solution
     * _moveManager - Interface that defines the methods for generating steps
     * _objectiveFunction - Interface that defines the methods for selecting steps
     */
    private int _numIter;
    private Solution _currentSolution;
    private IMoveManager _moveManager;
    private IObjectiveFunction _objectiveFunction;

    /**
     * Constructor the object Hill-Climber which content the optimization methods
     * @param objectiveFunction - Contains methods for selecting steps
     * @param moveManager - Contains methods for generating steps
     */
    public HillClimber(IObjectiveFunction objectiveFunction, IMoveManager moveManager) {
        _moveManager = moveManager;
        _objectiveFunction = objectiveFunction;
    }

    /**
     * Algorithm of Hill-Climber optimalization
     * @param solution - Object with an initial solution, which is made better
     * @param classic - Parameter that switchs between the classical and improved Hill-Climber algorithm
     */
    public void startSearching(Solution solution, String classic) {
        _currentSolution = solution;
        int iter = 0;
        double bestVal;

        /*cycle for repeat steps optimization algorithm*/
        while (iter < _numIter) {
            IMove[] moves = _moveManager.getAllMoves(_currentSolution);

            /*Checking version of Hill-Climber algorithm*/
            if (classic.equals("clasic") || classic.equals("Clasic")) {
                bestVal = Double.MAX_VALUE;
            } else {
                bestVal = solution.getObjectiveValue();
            }

            IMove best = null;
            double val;

            /*Browsing generated steps*/
            for (IMove m : moves) {
                val = _objectiveFunction.evaluateMove(_currentSolution, m);

                /*Selection of better solutions*/
                if (val < bestVal) {
                    best = m;
                    bestVal = val;
                }
            }

            /*Selection of the best actual solution*/
            if (best != null) {
                _currentSolution = best.apply(_currentSolution);
                _currentSolution.setObjectiveValue(bestVal);
            }

            _objectiveFunction.reset();
            iter++;
        }
    }

    /**
     * Method for setting number of iteration
     * @param count - Number of iteration algorithm
     */
    public void setIterationCount(int count) {
        _numIter = count;
    }

    /**
     * Function which return the best solution
     * @return - The best solution
     */
    public Solution getBestSolution() {
        return _currentSolution;
    }
}
