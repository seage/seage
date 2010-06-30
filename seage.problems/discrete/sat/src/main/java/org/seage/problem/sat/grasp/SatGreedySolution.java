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

import java.util.ArrayList;
import java.util.List;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 *
 * @author Martin Zaloga
 */
public class SatGreedySolution extends SatSolution {

     boolean[] _copyLitValues;

    /**
     * Constructor the solution with using the greedy algorithm for initial solution
     * @param cities - List of cities with their coordinates
     */
    public SatGreedySolution(Formula formula) throws Exception {
        super();
        initGreedySolution(formula);

    }

    //OK
    private void initGreedySolution(Formula formula) throws Exception {
        List<Integer> listOfLiteralsIndexes = new ArrayList();
        initLiterals(formula.getLiteralCount());
        int numLiterals = formula.getLiteralCount();
        for(int i = 0; i < numLiterals; i++){
            listOfLiteralsIndexes.add(i);
        }
        int listIndex;
        int literalIx;
        double valueBeforeMove = FormulaEvaluator.evaluate(formula, _litValues);
        double valueAfterMove;
        while(listOfLiteralsIndexes.size() != 0){
            listIndex = _rnd.nextInt(listOfLiteralsIndexes.size());
            literalIx = listOfLiteralsIndexes.get(listIndex);
            _copyLitValues = _litValues.clone();
            _copyLitValues[literalIx] = false;
            valueAfterMove = FormulaEvaluator.evaluate(formula, _copyLitValues);
            if(valueAfterMove < valueBeforeMove){
                _litValues[literalIx] = false;
                valueBeforeMove = valueAfterMove;
            }
            listOfLiteralsIndexes.remove(listIndex);
        }
        setObjectiveValue(FormulaEvaluator.evaluate(formula, _litValues));
    }
}