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

package org.seage.grammar;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import org.seage.data.DataNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * .
 * @author jenik
 */
public class NonterminalSymbol implements Symbol {
  private static final Logger log = LoggerFactory.getLogger(NonterminalSymbol.class.getName());
  /**
   * . 
   */
  private static final long serialVersionUID = -1930442234125327450L;

  public NonterminalSymbol(String name) {
    this.name = name;
    children = new Vector<Symbol>();
    rule = null;
  }

  public NonterminalSymbol(NonterminalSymbol other) {
    this(other.name);
  }

  @Override
  public Object eval(DataNode symbolTable) {
    try {
      return rule.eval(symbolTable, this);
    } catch (Exception ex) {
      log.error("GOT ERROR: {}", ex.getMessage(), ex);
      return null;
    }
  }

  @Override
  public Symbol optimize() {
    try {
      return rule.optimize(this);
    } catch (Exception ex) {
      log.error("GOT ERROR: {}", ex.getMessage(), ex);
      return null;
    }
  }

  /**
   * Expand nonterminal by given rule, on given stack.
   */
  public void expand(Stack<NonterminalSymbol> stack, GrammarRule rule) {
    /// save reference to rule
    this.rule = rule;
    /// add children
    Vector<Symbol> right = rule.getRight();
    Iterator<Symbol> it = right.iterator();
    int i = stack.size();
    while (it.hasNext()) {
      Symbol s = it.next();
      if (s.getType() == Symbol.Type.NONTERMINAL) {
        NonterminalSymbol sn = new NonterminalSymbol((NonterminalSymbol) s);
        children.add(sn);
        stack.add(i, sn);
      } else {
        TerminalSymbol ts = (TerminalSymbol) s.copy();
        ts.pick();
        children.add(ts);
      }
    }
  }

  public Vector<Symbol> getChildren() {
    return children;
  }

  @Override
  public String toString() {
    return name;
  }

  /** Get string representation of this subtree. */
  public String getStringTree() {
    String ret = "";
    for (Symbol s : children) {
      if (s.getType() == Symbol.Type.TERMINAL) {
        ret += s.toString();
      } else {
        ret += ((NonterminalSymbol) (s)).getStringTree();
      }
    }
    return ret;
  }

  public String getSymbolTree() {
    Iterator<Symbol> it = children.iterator();
    Iterator<?> rit = rule.getRight().iterator();
    String ret = "";
    while (rit.hasNext()) {
      Symbol s = (Symbol) rit.next();
      if (s.getType() == Symbol.Type.TERMINAL) {
        ret += s.toString();
      } else {
        ret += ((NonterminalSymbol) (it.next())).getStringTree();
      }
    }
    return ret;
  }

  /** Copy ourself, and return new instance. */
  @Override
  public Symbol copy() {
    return new NonterminalSymbol(this);
  }

  protected Vector<Symbol> children;
  protected GrammarRule rule; /// by which grammar rule were we expanded?
  protected String name;

  @Override
  public boolean equals(Object b) {
    return (b instanceof NonterminalSymbol) && ((NonterminalSymbol) b).name.equals(this.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public Type getType() {
    return Symbol.Type.NONTERMINAL;
  }

}
