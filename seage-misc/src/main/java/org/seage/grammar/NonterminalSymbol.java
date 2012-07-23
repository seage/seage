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
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.grammar;

import org.seage.data.DataNode;
import java.util.*;

/**
 *
 * @author jenik
 */
public class NonterminalSymbol implements Symbol {

    public NonterminalSymbol(String name) {
        this.name = name;
        children = new Vector<Symbol>();
        rule = null;
    }
    
    public NonterminalSymbol(NonterminalSymbol other) {
        this(other.name);
    }
    
    public Object eval(DataNode symbolTable) {
        try {
            return rule.eval(symbolTable, this);
        }
        catch (Exception e) {
            System.err.println("GOT ERROR: " + e);
            e.printStackTrace();
            return null;
        }
    }
    
    public Symbol optimize() {
        try {
            return rule.optimize(this);
        }
        catch (Exception e) {
            System.err.println("GOT ERROR: " + e);
            return null;
        }
    }
    
    /** @brief expand nontermial by given rule, on given stack
      * @param generator functor that will generate constant values 
      */
    public void expand(Stack<NonterminalSymbol> stack, GrammarRule rule) {
        ///save reference to rule
        this.rule = rule;
        ///add children        
        Vector<Symbol> right = rule.getRight();
        Iterator it = right.iterator();
        int i = stack.size();
        while (it.hasNext()) {
            Symbol s = (Symbol)it.next();
            if (s.getType() == Symbol.Type.NONTERMINAL) {
                NonterminalSymbol sn = new NonterminalSymbol((NonterminalSymbol)s);
                children.add(sn);
                stack.add(i, sn);
            }
            else {
                TerminalSymbol ts = (TerminalSymbol)s.copy();
                ts.pick();
                children.add(ts);
            }
        }
    }
    
    public Vector<Symbol> getChildren() {
        return children;
    }
    
    public String toString() {
        return name;
    }
    
    /** @brief get string representation of this subtree */
    public String getStringTree() 
    {        
        String ret = new String();
        for(Symbol s : children) {
            if (s.getType() == Symbol.Type.TERMINAL)
                ret += s.toString();
            else {
                ret += ((NonterminalSymbol)(s)).getStringTree();
            }
        }
        return ret;
    }
    
    public String getSymbolTree() {
        Iterator it = children.iterator();
        Iterator rit = rule.getRight().iterator();
        String ret = new String();
        while (rit.hasNext()) {
            Symbol s = (Symbol)rit.next();
            if (s.getType() == Symbol.Type.TERMINAL)
                ret += s.toString();
            else {
                ret += ((NonterminalSymbol)(it.next())).getStringTree();
            }
        }
        return ret;
    }
    
    /** @brief copy ourself, and return new instance */
    public Symbol copy() {
        return (Symbol) new NonterminalSymbol(this);
    }

            
    protected Vector<Symbol> children;
    protected GrammarRule rule;             ///by which grammar rule were we expanded?
    protected String name;
    
    public boolean equals(Object b) {
        return (b instanceof NonterminalSymbol) && ((NonterminalSymbol)b).name.equals(this.name);
    }
    
    public int hashCode() {
        return name.hashCode();
    }
    
    public Type getType() { return Symbol.Type.NONTERMINAL; }
    
}
