/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.metaheuristic.hillclimber;

/**
 *
 * @author rick
 */
public class HillClimber implements IHillClimber {

    private int _numIter;
    private Solution _currentSolution;
    private IMoveManager _moveManager;
    private IObjectiveFunction _objectiveFunction;

    public HillClimber(IObjectiveFunction objectiveFunction, IMoveManager moveManager) {
        this._moveManager = moveManager;
        this._objectiveFunction = objectiveFunction;
    }

    //TODO: A - review the code bellow
    public void startSearching(Solution solution) {
        this._currentSolution = solution;

        int iter = 0;
        while (iter < this._numIter) {
            IMove[] moves = this._moveManager.getAllMoves(this._currentSolution);

            IMove best = null;
            double bestVal = Double.MAX_VALUE;
            for (IMove m : moves) {
                double val = this._objectiveFunction.evaluateMove(this._currentSolution, m);

                if (val < bestVal) {
                    best = m;
                    bestVal = val;
                }
            }

            this._currentSolution = best.apply(this._currentSolution);
            iter++;
        }
    }

    public void setIterationCount(int count) {
        this._numIter = count;
    }

    public Solution getBestSolution() {
        return this._currentSolution;
    }
}
