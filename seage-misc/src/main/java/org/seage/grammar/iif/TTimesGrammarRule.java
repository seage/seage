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
public class TTimesGrammarRule extends GrammarRule {

  /**
   * 
   */
  private static final long serialVersionUID = 7324196344717287162L;

  public TTimesGrammarRule(int uniqueId) {
    super(new NonterminalSymbol("T"), new Vector<Symbol>(), uniqueId);
    Vector<Symbol> left = getRight();
    left.add(new NonterminalSymbol("T"));
    left.add(new TerminalSymbol("*", null));
    left.add(new NonterminalSymbol("F"));
  }

  /**
   * semantical actions for given rule
   * @param symbolTable table of symbols
   * @param treePos     position in parse tree (reference to left non terminal)
   */
  @Override
  public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
    // we should have 2 children
    Vector<Symbol> children = treePos.getChildren();
    if (children.size() != 3)
      throw new Exception("Arity error: expected 3 children, found: " + children.size());
    /// count the result
    Integer left = (Integer) (children.get(0).eval(symbolTable));
    children.get(1).eval(symbolTable);
    Integer right = (Integer) (children.get(2).eval(symbolTable));
    if (left != null && right != null)
      return left * right;
    return null;
  }

  /**
   * optimize derivate tree (eg. create result of arithmetical operations
   *        on contants)
   * @param treePos Tree position
   */
  @Override
  public Symbol optimize(NonterminalSymbol treePos) throws Exception {
    return null;
  }

}
