/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.problem.tsp.hillclimber;

/**
 *
 * @author rick
 */
public class TspMove
{
    int _ix1;
    int _ix2;

    public TspMove(int _ix1, int _ix2) {
        _ix1 = _ix1;
        _ix2 = _ix2;
    }



    public int getIx1() {
        return _ix1;
    }

    public int getIx2() {
        return _ix2;
    }

        
}
