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

    private Literal _literal;

    public SatMove(Literal literal) {
        _literal = literal;
    }

    public Literal getLiteral() {
        return _literal;
    }

    public Solution apply(Solution s) {
        SatSolution newSol = (SatSolution)s;
        for(int i = 0; i < newSol.getLiterals().length; i++){
            if(_literal.getAbsValue() == newSol.getLiterals()[i].getAbsValue()){
                newSol.getLiterals()[i].neg();
            }
        }
        return (Solution)newSol;
    }
}
