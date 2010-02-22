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

import java.util.ArrayList;
import java.util.List;
import org.seage.problem.sat.Formula;

/**
 *
 * @author Martin Zaloga
 */
public class SatGreedySolution extends SatSolution {

     private SatObjectiveFunction _objFce;
     private SatSolution _sol = new SatSolution();

    /**
     * Constructor the solution with using the greedy algorithm for initial solution
     * @param cities - List of cities with their coordinates
     */
    public SatGreedySolution(Formula formula) throws Exception {
        super();
        _litValues = new boolean[formula.getLiteralCount()];
        initGreedySolution(formula);
    }


    //OK
    private void initGreedySolution(Formula formula) throws Exception {
        List<Integer> listOfLiteralsIndexes = new ArrayList();
        _objFce = new SatObjectiveFunction(formula);
        initLiterals(formula.getLiteralCount());
        _sol.setLiteralValues(_litValues);
        int numLiterals = formula.getLiteralCount();
        for(int i = 0; i < numLiterals; i++){
            listOfLiteralsIndexes.add(i+1);
        }
        int listIndex;
        int literal;
        double valueBeforeMove = _objFce.evaluateMove(_sol, null);
        double valueAfterMove;
        while(listOfLiteralsIndexes.size() != 0){
            listIndex = _rnd.nextInt(listOfLiteralsIndexes.size());
            literal = listOfLiteralsIndexes.get(listIndex);
            valueAfterMove = _objFce.evaluateMove(_sol, new SatMove(literal));
            if(valueAfterMove < valueBeforeMove){
                _sol.negLiteral(literal);
                valueBeforeMove = valueAfterMove;
            }
            listOfLiteralsIndexes.remove(listIndex);
        }
    }
}