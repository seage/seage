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
import org.seage.metaheuristic.antcolony.Node;

/**
 *
 * @author Richard Malek
 */
public class FormulaEvaluator {
  private HashMap<Integer, Integer> literalsImpact;

  public FormulaEvaluator(Formula formula) {}

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

  public static int evaluate(Formula f, int id, boolean value) {
    int numTrueClauses = 0;

    for (Clause c : f.getClauses()) {
      for (Literal l : c.getLiterals()) {
        boolean neg = l.isNeg();
        if ((l.getId() == id) && ((value && !neg) || (!value && neg))) {
          numTrueClauses++;
          break;
        }
      }
    }
    return f.getClauses().size() - numTrueClauses;
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

}
