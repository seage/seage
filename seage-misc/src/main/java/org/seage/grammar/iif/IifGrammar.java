/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.grammar.iif;

import org.seage.grammar.Grammar;
import org.seage.grammar.NonterminalSymbol;
import java.util.Vector;

/**
 *
 * @author rick
 */
public class IifGrammar extends Grammar{

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
        addRule(new FParGrammarRule(18),-1);
        addRule(new FVariableGrammarRule(19), -1);
        addRule(new VariableGrammarRule(varNames, 20), -1);
          
    }

}
