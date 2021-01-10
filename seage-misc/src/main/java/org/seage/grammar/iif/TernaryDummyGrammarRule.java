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
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.grammar.iif;

import java.util.Vector;

import org.seage.data.DataNode;
import org.seage.grammar.GrammarRule;
import org.seage.grammar.NonterminalSymbol;
import org.seage.grammar.Symbol;

/**
 *
 * @author jenik E -> F
 */
public class TernaryDummyGrammarRule extends GrammarRule {

  /**
   * 
   */
  private static final long serialVersionUID = -3722771261823914549L;

  public TernaryDummyGrammarRule(int uniqueId) {
    super(new NonterminalSymbol("TERNARY"), new Vector<Symbol>(), uniqueId);
    Vector<Symbol> left = getRight();
    left.add(new NonterminalSymbol("E"));
  }

  /**
   * @brief semantical actions for given rule
   * @param symbolTable table of symbols
   * @param treePos     position in parse tree (reference to left non terminal)
   */
  @Override
  public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
    // we should have 1 children
    Vector<Symbol> children = treePos.getChildren();
    if (children.size() != 1)
      throw new Exception("Arity error: expected 1 children, found: " + children.size());
    /// count the result
    Object o = children.get(0).eval(symbolTable);
    return o;
  }

  /**
   * @brief optimize derivate tree (eg. create result of arithmetical operations
   *        on contants)
   */
  @Override
  public Symbol optimize(NonterminalSymbol treePos) throws Exception {
    return null;
  }

}
