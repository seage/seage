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
 * Contributors: Richard Malek - Initial implementation
 */
package org.seage.problem.sat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Richard Malek
 */
public class FormulaEvaluator {
  private HashMap<Integer, Integer> literalsFrequency;
  private HashMap<String, Integer> pairImpact;
  private Formula formula;

  public FormulaEvaluator(Formula formula) {
    this.formula = formula;

    this.literalsFrequency = new HashMap<>();
    this.pairImpact = new HashMap<>();

    for (Clause c : formula.getClauses()) {
      for (int i = 0; i < c.getLiterals().length; i++) {
        Literal lit1 = c.getLiterals()[i];
        for (int j = i + 1; j < c.getLiterals().length; j++) {
          Literal lit2 = c.getLiterals()[j];
          String id = String.format("%d%d", lit1.getId(), lit2.getId());

          // Todo

          this.pairImpact.put(id, 0);
        }

        int count = 0;
        int id = lit1.isNeg() ? -lit1.getId() : lit1.getId();
        if (this.literalsFrequency.containsKey(id)) {
          count = this.literalsFrequency.get(id);
        }
        count++;
        this.literalsFrequency.put(id, count);
      }
    }
  }

  public Map<Integer, Integer> getliteralsFrequency() {
    return literalsFrequency;
  }

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

}
