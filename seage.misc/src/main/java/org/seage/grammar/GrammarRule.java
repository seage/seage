/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.grammar;

import org.seage.data.DataNode;
import java.io.Serializable;
import java.util.*;

/**
 *
 * @author jenik
 */
abstract public class GrammarRule implements Serializable{
    
    /** @brief constructor
         @param left left side of rule
         @param right right side of rule
         @param uniqueId specifies id to compare grammar rules one to another
      */
    public GrammarRule(NonterminalSymbol left, Vector<Symbol> right, int uniqueId) {
        this.left = left;
        this.right = right;
        this.id = uniqueId;
    }
    
    public void setLeft(NonterminalSymbol s) {
        this.left = s;
    }
    
    public void setRight(Vector<Symbol> right) {
        this.right = right;
    }
    
    public NonterminalSymbol getLeft() {
        return left;
    }
    
    public Vector<Symbol> getRight() {
        return right;
    }
    
    /** @brief semantical actions for given rule
      * @param symbolTable table of symbols
      * @param treePos position in parse tree (reference to left non terminal)
      * @param constants chromosome to generate constants
      */
    public Object eval(DataNode symbolTable, NonterminalSymbol treePos) throws Exception {
        Iterator it = treePos.getChildren().iterator();
        while (it.hasNext()) {
            ((Symbol)it.next()).eval(symbolTable);
        }
        return null;    //undefined operation, cannot really eval them
    }
        
    /** @brief optimize derivate tree (eg. create result of arithmetical operations on contants) */
    abstract public Symbol optimize(NonterminalSymbol treePos) throws Exception;
        
    public boolean equals(Object b) {
        return (b instanceof GrammarRule) && ((GrammarRule)b).id == this.id;
    }
    
    public int hashCode() {
        return new Integer(id).hashCode();
    }
    
    public String toString() {
        String s = left + " -> ";
        Iterator it = right.iterator();
        while (it.hasNext())
            s += it.next();
        return s;
    }
    
    
    protected Vector<Symbol> right;
    protected NonterminalSymbol left;
    protected int id;

}
