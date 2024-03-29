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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.grammar.iif;

import java.util.Vector;

import org.seage.data.DataNode;
import org.seage.grammar.GrammarRule;
import org.seage.grammar.NonterminalSymbol;
import org.seage.grammar.Symbol;
import org.seage.grammar.TerminalSymbol;

/**
 *
 * @author jenik T -> T * F
 */
public class StatementGrammarRule extends GrammarRule {

  /**
  * 
  */
  private static final long serialVersionUID = 8648379993044517875L;

  public StatementGrammarRule(int uniqueId) {
    super(new NonterminalSymbol("STATEMENT"), new Vector<Symbol>(), uniqueId);
    Vector<Symbol> left = getRight();
    left.add(new NonterminalSymbol("L_VALUE"));
    left.add(new TerminalSymbol("=", null));
    left.add(new NonterminalSymbol("TERNARY"));
    // left.add(new TerminalSymbol("\n", null));
    // /* NOTE: 2 more to increase count of generated statements */
    // left.add(new NonterminalSymbol("STATEMENT"));
    // left.add(new NonterminalSymbol("STATEMENT"));
  }

  /**
   * semantical actions for given rule
   * @param symbolTable table of symbols
   * @param treePos     position in parse tree (reference to left non terminal)
   */
  @Override
  public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
    // we should have 6 children
    Vector<Symbol> children = treePos.getChildren();
    if (children.size() != 3)
      throw new Exception("Arity error: expected 6 children, found: " + children.size());
    (children.get(0).eval(symbolTable)).toString();
    Integer right = (Integer) (children.get(2).eval(symbolTable));
    // if (left != null && right != null) {
    // symbolTable.putValue(left, right);
    // }
    // TODO: Fix following
    Object o = null;// children.get(4).eval(symbolTable);
    return null; // o == null ? right : o;
  }

  /**
   * optimize derivate tree (eg. create result of arithmetical operations
   *        on contants)
   */
  @Override
  public Symbol optimize(NonterminalSymbol treePos) throws Exception {
    return null;
  }

}
