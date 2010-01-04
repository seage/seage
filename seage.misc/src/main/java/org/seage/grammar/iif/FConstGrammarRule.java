/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.grammar.iif;

import org.seage.data.DataNode;
import ailibrary.grammar.Symbol;
import ailibrary.grammar.GrammarRule;
import ailibrary.grammar.IntTerminalSymbol;
import ailibrary.grammar.NonterminalSymbol;
import java.util.*;

/**
 *
 * @author jenik
   F -> a
 */
public class FConstGrammarRule extends GrammarRule {
    
    public FConstGrammarRule(int uniqueId) {
        super (new NonterminalSymbol("F"), new Vector<Symbol> (), uniqueId);
//        Vector<ISymbol> right = getRight();
        IntTerminalSymbol it = new IntTerminalSymbol("const", uniqueId);
        it.setDomain(0,100);
        right.add(it);
    }
            
    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
        //we should have one children - constant value
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
