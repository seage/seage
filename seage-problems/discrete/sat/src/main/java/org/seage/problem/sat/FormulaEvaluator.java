/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.

 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.

 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.sat;

import java.util.HashMap;
import java.util.List;
import org.seage.metaheuristic.antcolony.Node;

/**
 * .

 * @author Richard Malek
 */
public class FormulaEvaluator {
  private HashMap<Integer, Integer> literalImpact;
  private HashMap<Integer, Integer> literalPairImpact;
  private Formula formula;

  /**
   * Constructor.
   *
   * @param formula .
   */
  public FormulaEvaluator(Formula formula) {
    this.formula = formula;

    this.literalImpact = new HashMap<>();
    this.literalPairImpact = new HashMap<>();

    for (Clause c : formula.getClauses()) {
      for (int i = 0; i < c.getLiterals().length; i++) {
        Literal lit1 = c.getLiterals()[i];
        int count = 0;
        int id = lit1.isNeg() ? -lit1.getId() : lit1.getId();
        if (this.literalImpact.containsKey(id)) {
          count = this.literalImpact.get(id);
        }
        count++;
        this.literalImpact.put(id, count);
      }
    }

    for (int i = 1; i <= formula.getLiteralCount(); i++) {
      int l11 = -i;
      int l12 = i;
      for (int j = i + 1; j <= formula.getLiteralCount(); j++) {
        int l21 = -j;
        int l22 = j;
        this.literalPairImpact.put(hash(l11, l21), evaluateLiteralPair(formula, l11, l21));
        this.literalPairImpact.put(hash(l11, l22), evaluateLiteralPair(formula, l11, l22));
        this.literalPairImpact.put(hash(l12, l21), evaluateLiteralPair(formula, l12, l21));
        this.literalPairImpact.put(hash(l12, l22), evaluateLiteralPair(formula, l12, l22));
      }
    }
  }

  /**
   * Hash used for pairs.
   *
   * @param x Id of first literal.
   * @param y Id of second literal.
   * @return Hash.
   */
  // test needed
  private int hash(int x, int y) {
    return x < y ? y * y + x + y : x * x + x + y;
  }

  /**
   * Method returns the element from singleImpact map.
   *
   * @param literalId Literal id.
   * @return SingleImpact element if id is present, 0 otherwise.
   */
  public int getLiteralImpact(int literalId) {
    if (!literalImpact.containsKey(literalId)) {
      return 0;
    }
    return literalImpact.get(literalId);
  }

  /**
   * Method returns the element from pairImpact map.
   *
   * @param literal1Value First literal value.
   * @param literal2Value Second literal value.
   * @return PairImpact value if literals present, 0 otherwise.
   */
  public int getLiteralPairImpact(int literal1Value, int literal2Value) {
    // Check if zero value
    if (literal1Value == 0 || literal2Value == 0) {
      return 0;
    }

    int key = hash(literal1Value, literal2Value);
    if (!literalPairImpact.containsKey(key)) {
      return 0;
    }
    return literalPairImpact.get(key);
  }

  /**
   * Method evaluates formula using given nodes.
   *
   * @param f Formula.
   * @param nodes Nodes.
   * @return Number of false clauses.
   */
  public static int evaluate(Formula f, List<Node> nodes) {
    Boolean[] s = new Boolean[f.getLiteralCount()];

    for (Node n : nodes) {
      if (n.getID() == 0) {
        continue;
      }
      s[Math.abs(n.getID()) - 1] = n.getID() > 0;
    }
    return evaluate(f, s);
  }

  /**
   * Method evaluates formula using given literal truth values.
   *
   * @param f Formula.
   * @param s Truth values of literals.
   * @return Number of false clauses.
   */
  public static int evaluate(Formula f, Boolean[] s) {
    int numTrueClauses = 0;

    for (Clause c : f.getClauses()) {
      for (Literal l : c.getLiterals()) {
        Boolean x = s[l.getId() - 1];
        boolean neg = l.isNeg();
        if ((x != null) && ((x && !neg) || (!x && neg))) {
          numTrueClauses++;
          break;
        }
      }
    }
    return f.getClauses().size() - numTrueClauses;
  }

  /**
   * Method evaluates formula with given literal truth value.
   *
   * @param id Literal id.
   * @param value Truth value of given literal.
   * @return Number of false clauses.
   */
  public int evaluate(int id, boolean value) {
    int numTrueClauses = 0;

    for (Clause c : formula.getClauses()) {
      for (Literal l : c.getLiterals()) {
        boolean neg = l.isNeg();
        if ((l.getId() == id) && ((value && !neg) || (!value && neg))) {
          numTrueClauses++;
          break;
        }
      }
    }
    return formula.getClauses().size() - numTrueClauses;
  }

  /**
   * Method evaluates formula using given pair.
   *
   * @param f Formula.
   * @param literal1Value First literal (negative or positive id of literal).
   * @param literal2Value Second literal (negative or positive id of literal).
   * @return Number of true pairs (clauses where both literal has true value).
   */
  public static int evaluateLiteralPair(Formula f, int literal1Value, int literal2Value) {
    int numTruePairs = 0;

    for (Clause c : f.getClauses()) {
      boolean lit1ok = false;
      boolean lit2ok = false;
      for (Literal l : c.getLiterals()) {
        int lv = l.getValue();
        if (lv == literal1Value) {
          lit1ok = true;
        }
        if (lv == literal2Value) {
          lit2ok = true;
        }
        if (lit1ok && lit2ok) {
          numTruePairs++;
          break;
        }
      }
    }
    return numTruePairs;
  }

}
