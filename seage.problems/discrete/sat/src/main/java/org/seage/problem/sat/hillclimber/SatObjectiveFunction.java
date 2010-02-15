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
import org.seage.metaheuristic.hillclimber.IObjectiveFunction;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatObjectiveFunction implements IObjectiveFunction {

    public Literal[] _literals;
    SatSolution _sol = new SatSolution();
    Formula _readedFormula;

    public SatObjectiveFunction(Formula readedFormula) {
        _readedFormula = readedFormula;
    }

    public void reset() {
    }

    //OK
    public double evaluateMove(Solution sol, IMove mov) {
        SatSolution solution = CopySolution((SatSolution)sol);
        SatMove move = (SatMove)mov;
        _sol = (SatSolution)move.apply(solution);
        _readedFormula.substituteLiteralsInFormula(_sol);
        //System.out.println(""+_readedFormula.numberFalseClausesAfterSubstitute());
        return _readedFormula.numberFalseClausesAfterSubstitute();
    }

    //OK
    public SatSolution CopySolution(SatSolution sol){
        SatSolution newSol = new SatSolution();
        newSol.setLiterals(sol.copyLiterals());
        return newSol;
    }

    //OK
    public void printLiterals(){
        for(int i = 0; i < _literals.length; i++){
            System.out.println("literal["+i+"] = "+_literals[i]);
        }
    }
}