/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.grammar;

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
