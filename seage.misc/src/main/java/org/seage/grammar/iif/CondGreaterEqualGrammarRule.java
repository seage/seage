/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.grammar.iif;

import org.seage.data.DataNode;
import ailibrary.grammar.Symbol;
import ailibrary.grammar.GrammarRule;
import ailibrary.grammar.NonterminalSymbol;
import ailibrary.grammar.TerminalSymbol;
import java.util.*;


/**
 *
 * @author jenik
   T -> T * F
 */
public class CondGreaterEqualGrammarRule extends GrammarRule {
    
    public CondGreaterEqualGrammarRule(int uniqueId) {
        super (new NonterminalSymbol("COND"), new Vector<Symbol> (), uniqueId);
        Vector<Symbol> left = getRight();
        left.add(new NonterminalSymbol("E"));
        left.add(new TerminalSymbol(">=", null));
        left.add(new NonterminalSymbol("E"));
    }
            
    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
        //we should have 2 children
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 3)
            throw new Exception("Arity error: expected 3 children, found: " + children.size());
        ///count the result
        Integer left = (Integer)(children.get(0).eval(symbolTable));
        children.get(1).eval(symbolTable);
        Integer right = (Integer)(children.get(2).eval(symbolTable));
        if (left != null && right != null) {
            return left >= right;
        }
        return null;
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception {
        return null;
    }
    
}
