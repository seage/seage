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
package org.seage.problem.sat.grasp;

import org.seage.metaheuristic.grasp.IMove;
import org.seage.metaheuristic.grasp.Solution;

/**
 *
 * @author Martin Zaloga
 */
public class SatMove implements IMove {

    private int _literalIx;

    public SatMove(int literalIx) {
        literalIx = _literalIx;
    }

    public int getLiteralIx() {
        return _literalIx;
    }
 
    public Solution apply(Solution s) {
        // TODO: A - tady se musi 's' klonovat - (hluboka kopie)
        SatSolution newSol = (SatSolution)s;
        boolean[] litValues = newSol._litValues.clone();
        if(litValues[_literalIx]){
            litValues[_literalIx] = false;
        } else {
            litValues[_literalIx] = true;
        }
        newSol = new SatSolution();
        newSol.setLiteralValues(litValues);
        return (Solution)newSol;
    }
}
