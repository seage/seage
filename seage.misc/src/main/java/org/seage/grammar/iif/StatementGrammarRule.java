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
public class StatementGrammarRule extends GrammarRule {
    
        public StatementGrammarRule(int uniqueId) {
        super (new NonterminalSymbol("STATEMENT"), new Vector<Symbol> (), uniqueId);
        Vector<Symbol> left = getRight();
        left.add(new NonterminalSymbol("L_VALUE"));
        left.add(new TerminalSymbol("=", null));
        left.add(new NonterminalSymbol("TERNARY"));
//        left.add(new TerminalSymbol("\n", null));
//        /* NOTE: 2 more to increase count of generated statements */
//        left.add(new NonterminalSymbol("STATEMENT"));
//        left.add(new NonterminalSymbol("STATEMENT"));
    }
            
    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      */
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
        //we should have 6 children
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 3)
            throw new Exception("Arity error: expected 6 children, found: " + children.size());
        ///count the result
        String left = (children.get(0).eval(symbolTable)).toString();
        Integer right = (Integer)(children.get(2).eval(symbolTable));
//        if (left != null && right != null) {
//            symbolTable.putValue(left, right);
//        }
        Object o = null;//children.get(4).eval(symbolTable);
        return o == null ? right : o;
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception {
        return null;
    }
    
}
