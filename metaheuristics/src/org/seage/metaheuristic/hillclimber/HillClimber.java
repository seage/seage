/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.metaheuristic.hillclimber;

/**
 *
 * @author rick
 */
public class HillClimber implements IHillClimber
{
    private int _numIter;
    private Solution _currentSolution;

    private IMoveManager _moveManager;
    private IObjectiveFunction _objectiveFunction;

    public HillClimber(IObjectiveFunction objectiveFunction, IMoveManager moveManager)
    {
        _moveManager = moveManager;
        _objectiveFunction = objectiveFunction;
    }

    //TODO: A - review the code bellow
    public void startSearching(Solution solution)
    {
        _currentSolution = solution;

        int iter = 0;
        while(iter < _numIter)
        {
            IMove[] moves = _moveManager.getAllMoves(_currentSolution);

            IMove best = null;
            for(IMove m : moves)
            {
                double bestVal = Double.MAX_VALUE;

                double val = _objectiveFunction.evaluateMove(_currentSolution, m);

                if(val < bestVal)
                    best = m;
            }

            _currentSolution = best.apply(_currentSolution);
            iter++;
        }
    }

    public void setIterationCount(int count) {
        _numIter = count;
    }



    public Solution getBestSolution()
    {
        return _currentSolution;
    }



}
