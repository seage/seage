/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.seage.grammar.iif;

import org.seage.data.DataNode;
import org.seage.grammar.Symbol;
import org.seage.grammar.GrammarRule;
import org.seage.grammar.NonterminalSymbol;
import java.util.*;


/**
 *
 * @author jenik
   E -> F
 */
public class TDummyGrammarRule extends GrammarRule {
    
    public TDummyGrammarRule(int uniqueId) {
        super (new NonterminalSymbol("T"), new Vector<Symbol> (), uniqueId);
        Vector<Symbol> left = getRight();
        left.add(new NonterminalSymbol("F"));
    }
            
    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
        //we should have 1 children
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 1)
            throw new Exception("Arity error: expected 1 children, found: " + children.size());    
        ///count the result
        return children.get(0).eval(symbolTable);
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception {
        return null;
    }
    
}
