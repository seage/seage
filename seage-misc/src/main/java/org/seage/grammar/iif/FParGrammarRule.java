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

import org.seage.data.DataNode;
import org.seage.grammar.Symbol;
import org.seage.grammar.GrammarRule;
import org.seage.grammar.NonterminalSymbol;
import org.seage.grammar.TerminalSymbol;
import java.util.*;

/**
 *
 * @author jenik
   F -> a
 */
public class FParGrammarRule extends GrammarRule
{

    /**
     * 
     */
    private static final long serialVersionUID = 18832664704801847L;

    public FParGrammarRule(int uniquId)
    {
        super(new NonterminalSymbol("F"), new Vector<Symbol>(), uniquId);
        Vector<Symbol> right = getRight();
        right.add(new TerminalSymbol("(", null));
        right.add(new NonterminalSymbol("E"));
        right.add(new TerminalSymbol(")", null));
    }

    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception
    {
        //we should have one children - constant value
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 3)
            throw new Exception("Arity error: expected 3 children, found: " + children.size());
        ///count the result
        return children.get(1).eval(symbolTable);
    }

    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception
    {
        return null;
    }

}
