/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.Solution;

/**
 *
 * @author rick
 */
public class TspMove implements IMove
{

    int _ix1;
    int _ix2;

    public TspMove(int _ix1, int _ix2) {
        this._ix1 = _ix1;
        this._ix2 = _ix2;
    }

    public int getIx1() {
        return _ix1;
    }

    public int getIx2() {
        return _ix2;
    }

    public Solution apply(Solution s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
