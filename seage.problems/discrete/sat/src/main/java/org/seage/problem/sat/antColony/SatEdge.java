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
package org.seage.problem.sat.antColony;

import org.seage.metaheuristic.antcolony.Edge;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

public class SatEdge extends Edge {
    FormulaEvaluator _evaluator = new FormulaEvaluator();

    public SatEdge(SatNode start, SatNode end, double locEvaporCoeff, Formula formula, boolean[] preparedSolution) {
        super(start, end, locEvaporCoeff);
        setEdgeLengthFrom1in2(_evaluator.evaluate(formula, createSol(end, formula, preparedSolution)));
        setEdgeLengthFrom2in1(_evaluator.evaluate(formula, createSol(start, formula, preparedSolution)));
    }

    public boolean[] createSol(SatNode node, Formula formula, boolean[] preparedSolution){
        boolean[] solution = preparedSolution.clone();
        int index = node.getId()%formula.getLiteralCount();
        solution[index] = node.getValue();
        return solution;
    }
}