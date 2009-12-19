/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://seage.sourceforge.net/license/cpl-v10.html
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */
package org.seage.problem.tsp.hillclimber;

import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.Solution;

/**
 *
 * @author Richard Malek
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
        TspSolution newSol = (TspSolution) s;
        Integer[] newTour = newSol.getTour();
        int pom = newTour[_ix1];
        newTour[_ix1] = newTour[_ix2];
        newTour[_ix2] = pom;
        newSol.setTour(newTour);
        return (Solution) newSol;
        //throw new UnsupportedOperationException("Not supported yet.");
    }


}
