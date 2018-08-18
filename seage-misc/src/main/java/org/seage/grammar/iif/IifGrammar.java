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

import org.seage.grammar.Grammar;
import org.seage.grammar.NonterminalSymbol;

/**
 *
 * @author Richard Malek
 */
public class IifGrammar extends Grammar
{

    public IifGrammar(Vector<String> varNames)
    {
        super(new NonterminalSymbol("STATEMENT"));

        addRule(new StatementGrammarRule(1), -1);
        addRule(new LValueGrammarRule(2), -1);
        addRule(new TernaryGrammarRule(3), -1);
        addRule(new TernaryDummyGrammarRule(4), -1);
        addRule(new CondLessGrammarRule(5), -1);
        addRule(new CondGreaterGrammarRule(6), -1);
        addRule(new CondEqualGrammarRule(7), -1);
        addRule(new CondNotEqualGrammarRule(8), -1);
        addRule(new CondGreaterEqualGrammarRule(9), -1);
        addRule(new CondLessEqualGrammarRule(10), -1);
        addRule(new EPlusGrammarRule(11), 1);
        addRule(new EMinusGrammarRule(12), 1);
        addRule(new EDummyGrammarRule(13), 1);
        addRule(new TTimesGrammarRule(14), -1);
        addRule(new TDivideGrammarRule(15), -1);
        addRule(new TDummyGrammarRule(16), -1);
        addRule(new FConstGrammarRule(17), -1);
        addRule(new FParGrammarRule(18), -1);
        addRule(new FVariableGrammarRule(19), -1);
        addRule(new VariableGrammarRule(varNames, 20), -1);

    }

}
