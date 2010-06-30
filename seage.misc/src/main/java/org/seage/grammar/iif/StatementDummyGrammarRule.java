/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.grammar.iif;

import ailibrary.grammar.Symbol;
import ailibrary.grammar.GrammarRule;
import ailibrary.grammar.NonterminalSymbol;
import java.util.*;

/**
 * @deprecated 
 * @author jenik
   E -> F
 */
public class StatementDummyGrammarRule extends GrammarRule {
    
    public StatementDummyGrammarRule(int uniqueId) {
        super (new NonterminalSymbol("STATEMENT"), new Vector<Symbol> (), uniqueId);
    }
            
    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    public Object eval(HashMap symbolTable, NonterminalSymbol treePos) throws Exception {
        //we should have 1 children
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 0)
            throw new Exception("Arity error: expected 0 children, found: " + children.size());    
        ///count the result
        return null;
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception {
        return null;
    }
    
}
