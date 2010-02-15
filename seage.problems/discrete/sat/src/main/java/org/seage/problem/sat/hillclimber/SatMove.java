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
package org.seage.problem.sat.hillclimber;

import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatMove implements IMove {

    private int _literalIx;

    public SatMove(int literalIx) {
        literalIx = _literalIx;
    }
 
    public Solution apply(Solution s) {
        // TODO: A - tady se musi 's' klonovat - (hluboka kopie)
        SatSolution newSol = (SatSolution)s;
        for(int i = 0; i < newSol.getLiterals().length; i++){
            if(_literal.getAbsValue() == newSol.getLiterals()[i].getAbsValue()){
                newSol.getLiterals()[i].neg();
            }
        }
        return (Solution)newSol;
    }
}
