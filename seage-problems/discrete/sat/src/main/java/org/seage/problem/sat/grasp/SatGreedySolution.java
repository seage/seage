/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Martin Zaloga - Initial implementation
 */

package org.seage.problem.sat.grasp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

/**
 * .
 * @author Martin Zaloga
 */
public class SatGreedySolution extends SatSolution {
  private static final long serialVersionUID = -4353469001304258494L;
  private Random rnd = new Random(); // sonar fix
  Boolean[] _copyLitValues;

  /**
   * Constructor the solution with using the greedy algorithm for initial solution.
   * 
   * @param formula - List of clauses representing the formula
   */
  public SatGreedySolution(Formula formula, FormulaEvaluator formulaEvaluator) throws Exception {
    super(formula, formulaEvaluator);
    initGreedySolution();

  }

  // OK
  private void initGreedySolution() throws Exception {
    List<Integer> listOfLiteralsIndexes = new ArrayList<>();
    initLiterals(formula.getLiteralCount());
    int numLiterals = formula.getLiteralCount();
    for (int i = 0; i < numLiterals; i++) {
      listOfLiteralsIndexes.add(i);
    }
    int listIndex;
    int literalIx;
    double valueBeforeMove = formulaEvaluator.evaluate(formula, _litValues);
    double valueAfterMove;
    while (listOfLiteralsIndexes.isEmpty()) {
      listIndex = rnd.nextInt(listOfLiteralsIndexes.size());
      literalIx = listOfLiteralsIndexes.get(listIndex);
      _copyLitValues = _litValues.clone();
      _copyLitValues[literalIx] = false;
      valueAfterMove = formulaEvaluator.evaluate(formula, _copyLitValues);
      if (valueAfterMove < valueBeforeMove) {
        _litValues[literalIx] = false;
        valueBeforeMove = valueAfterMove;
      }
      listOfLiteralsIndexes.remove(listIndex);
    }
    setObjectiveValue(formulaEvaluator.evaluate(formula, _litValues));
  }
}
