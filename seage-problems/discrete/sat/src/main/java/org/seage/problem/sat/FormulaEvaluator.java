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
  private HashMap<Integer, Integer> singleImpact;
  private HashMap<Integer, Integer> pairImpact;
  private Formula formula;

  /**
   * Constructor.
   *
   * @param formula .
   */
  public FormulaEvaluator(Formula formula) {
    this.formula = formula;

    this.singleImpact = new HashMap<>();
    this.pairImpact = new HashMap<>();

    for (Clause c : formula.getClauses()) {
      for (int i = 0; i < c.getLiterals().length; i++) {
        Literal lit1 = c.getLiterals()[i];
        int count = 0;
        int id = lit1.isNeg() ? -lit1.getId() : lit1.getId();
        if (this.singleImpact.containsKey(id)) {
          count = this.singleImpact.get(id);
        }
        count++;
        this.singleImpact.put(id, count);
      }
    }

    for (int i = 1; i <= formula.getLiteralCount(); i++) {
      int l11 = -i;
      int l12 = i;
      for (int j = i + 1; j <= formula.getLiteralCount(); j++) {
        int l21 = -j;
        int l22 = j;
        this.pairImpact.put(hash(l11, l21), evaluatePair(formula, l11, l21));
        this.pairImpact.put(hash(l11, l22), evaluatePair(formula, l11, l22));
        this.pairImpact.put(hash(l12, l21), evaluatePair(formula, l12, l21));
        this.pairImpact.put(hash(l12, l22), evaluatePair(formula, l12, l22));
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
  private int hash(int x, int y) {
    return x < y ? y * y + x + y : x * x + x + y;
  }

  /**
   * Method returns the element from singleImpact map.
   *
   * @param literalId Literal id.
   * @return SingleImpact element if id is present, 0 otherwise.
   */
  public int getSingleImpact(int literalId) {
    if (!singleImpact.containsKey(literalId)) {
      return 0;
    }
    return singleImpact.get(literalId);
  }

  /**
   * Method returns the element from pairImpact map.
   *
   * @param literal1Id First literal id.
   * @param literal2Id Second literal id.
   * @return PairImpact element if ids present, 0 otherwise.
   */
  public int getPairImpact(int literal1Id, int literal2Id) {
    int key = hash(literal1Id, literal2Id);
    if (!pairImpact.containsKey(key)) {
      return 0;
    }
    return pairImpact.get(key);
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
   * @param id1 First literal (negative or positive id of literal).
   * @param id2 Second literal (negative or positive id of literal).
   * @return Number of true pairs (clauses where both literal has true value).
   */
  public static int evaluatePair(Formula f, int id1, int id2) {
    int numTruePairs = 0;

    for (Clause c : f.getClauses()) {
      boolean id1ok = false;
      boolean id2ok = false;
      for (Literal l : c.getLiterals()) {
        int lv = l.isNeg() ? -l.getId() : l.getId();
        if (lv == id1) {
          id1ok = true;
        }
        if (lv == id2) {
          id2ok = true;
        }
        if (id1ok && id2ok) {
          numTruePairs++;
          break;
        }
      }
    }
    return numTruePairs;
  }

}
