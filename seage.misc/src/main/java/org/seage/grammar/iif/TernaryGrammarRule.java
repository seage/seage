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
   E -> F
 */
public class TernaryGrammarRule extends GrammarRule {
    
    public TernaryGrammarRule(int uniqueId) {
        super (new NonterminalSymbol("TERNARY"), new Vector<Symbol> (), uniqueId);
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
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
        //we should have 7 children
        Vector<Symbol> children = treePos.getChildren();
        if (children.size() != 7)
            throw new Exception("Arity error: expected 7 children, found: " + children.size());    
        ///count the result
        //NOTE: eval both branches to repair wrong divisions (python interprets both branches)
        Object t = children.get(1).eval(symbolTable);
        Object f = children.get(3).eval(symbolTable);
        if (t == null || f == null) {
            return null;
        }
        return ((Boolean)(children.get(5).eval(symbolTable))) ? t : f;
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception {
        return null;
    }
    
}
