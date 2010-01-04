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
import ailibrary.grammar.IntGeneratorFunctor;
import java.util.*;

/**
 *
 * @author jenik
   F -> a
 */
public class VariableGrammarRule extends GrammarRule {
    
    public VariableGrammarRule(Vector<String> names, int uniqueId) {
        super (new NonterminalSymbol("VAR"), new Vector<Symbol> (), uniqueId);
//        Vector<ISymbol> right = getRight();
        TerminalSymbol vt = new TerminalSymbol(names, new IntGeneratorFunctor(uniqueId));
        right.add(vt);
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
        Object o = children.get(0).eval(symbolTable);
        return o;
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    public Symbol optimize(NonterminalSymbol treePos) throws Exception {
        return null;
    }
    
}
