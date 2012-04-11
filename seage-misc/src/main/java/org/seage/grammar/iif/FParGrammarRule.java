/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class FParGrammarRule extends GrammarRule {
    
    public FParGrammarRule(int uniquId) {
        super (new NonterminalSymbol("F"), new Vector<Symbol> (), uniquId);
        Vector<Symbol> right = getRight();
        right.add(new TerminalSymbol("(", null));
        right.add(new NonterminalSymbol("E"));
        right.add(new TerminalSymbol(")", null));
    }
            
    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
        //we should have one children - constant value
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 3)
            throw new Exception("Arity error: expected 3 children, found: " + children.size());        
        ///count the result
        return children.get(1).eval(symbolTable);
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception {
        return null;
    }
    
}
