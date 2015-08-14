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
import org.seage.grammar.TerminalSymbol;

/**
 *
 * @author jenik
   E -> F
 */
public class TernaryGrammarRule extends GrammarRule
{

    /**
     * 
     */
    private static final long serialVersionUID = -4817218077095377037L;

    public TernaryGrammarRule(int uniqueId)
    {
        super(new NonterminalSymbol("TERNARY"), new Vector<Symbol>(), uniqueId);
        Vector<Symbol> left = getRight();
        /*        left.add(new NonterminalSymbol("COND"));
        left.add(new TerminalSymbol("?", null));
        left.add(new NonterminalSymbol("TERNARY"));
        left.add(new TerminalSymbol(":", null));
        left.add(new NonterminalSymbol("TERNARY")); */
        left.add(new TerminalSymbol("(True: ", null));
        left.add(new NonterminalSymbol("TERNARY"));
        left.add(new TerminalSymbol(", False: ", null));
        left.add(new NonterminalSymbol("TERNARY"));
        left.add(new TerminalSymbol(")[", null));
        left.add(new NonterminalSymbol("COND"));
        left.add(new TerminalSymbol("]", null));
    }

    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    @Override
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception
    {
        //we should have 7 children
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 7)
            throw new Exception("Arity error: expected 7 children, found: " + children.size());
        ///count the result
        //NOTE: eval both branches to repair wrong divisions (python interprets both branches)
        Object t = children.get(1).eval(symbolTable);
        Object f = children.get(3).eval(symbolTable);
        if (t == null || f == null)
        {
            return null;
        }
        return ((Boolean) (children.get(5).eval(symbolTable))) ? t : f;
    }

    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    @Override
    public Symbol optimize(NonterminalSymbol treePos) throws Exception
    {
        return null;
    }

}
