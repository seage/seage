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

import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Martin Zaloga
 */
public class SatRandomSolution extends SatSolution {
    private int _countLiterals;

    public SatRandomSolution(Formula formula) {
        super();
        _countLiterals = formula.getLiteralCount();
        initRandSol(formula);
    }

    private void initRandSol(Formula formula) {
        _litValues = new boolean[_countLiterals];
        for (int i = 0; i < _countLiterals; i++) {
            _litValues[i] = _rnd.nextBoolean();
        }
        setObjectiveValue(FormulaEvaluator.evaluate(formula, _litValues));
    }
}
