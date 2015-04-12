/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *     Martin Zaloga
 *     - Initial implementation
 */
package org.seage.problem.sat.grasp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        List<Integer> listOfLiteralsIndexes = new ArrayList<Integer>();
        initLiterals(formula.getLiteralCount());
        int numLiterals = formula.getLiteralCount();
        for(int i = 0; i < numLiterals; i++){
            listOfLiteralsIndexes.add(i);
        }
        int listIndex;
        int literalIx;
        double valueBeforeMove = FormulaEvaluator.evaluate(formula, _litValues);
        double valueAfterMove;
        Random rnd = new Random();
        while(listOfLiteralsIndexes.size() != 0){
            listIndex = rnd.nextInt(listOfLiteralsIndexes.size());
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
