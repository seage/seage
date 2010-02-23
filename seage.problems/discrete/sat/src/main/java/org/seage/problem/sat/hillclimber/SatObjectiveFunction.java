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

import java.io.Serializable;
import org.seage.data.ObjectCloner;
import org.seage.metaheuristic.hillclimber.IMove;
import org.seage.metaheuristic.hillclimber.IObjectiveFunction;
import org.seage.metaheuristic.hillclimber.Solution;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.Literal;

/**
 *
 * @author Martin Zaloga
 */
public class SatObjectiveFunction implements IObjectiveFunction, Serializable {

    private Formula _formula;

    public SatObjectiveFunction(Formula formula) {
        _formula = formula;
    }

    public void reset() {
    }

    //OK
    public double evaluateMove(Solution sol, IMove move) throws Exception
    {

        if(move == null)
            return evaluate((SatSolution)sol);
        else
        {
            SatSolution s = (SatSolution)ObjectCloner.deepCopy(sol);
            move.apply(s);
            return evaluate(s);
        }
    }

    private int evaluate(SatSolution sol)
    {
        return FormulaEvaluator.evaluate(_formula, sol.getLiteralValues());
    }

}