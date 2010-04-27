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

    public SatEdge(SatNode start, SatNode end, double locEvaporCoeff, Formula formula, boolean[] preparedSolution) {
        super(start, end, locEvaporCoeff);
            setEdgeLength(FormulaEvaluator.evaluate(formula, createSol(end, formula, preparedSolution)));
    }

    public boolean[] createSol(SatNode node, Formula formula, boolean[] preparedSolution) {
        boolean[] solution = preparedSolution.clone();
        int index = Math.abs(node.getId());
        solution[index - 1] = node.getValue();
        return solution;
    }
}
