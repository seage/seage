/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.grammar;

import org.seage.data.DataNode;
import java.util.*;
/**
 *
 * @author jenik
 */
public class TerminalSymbol implements Symbol {

    public TerminalSymbol(Vector<String> names, Functor generator) {
        this.names = names;
        this.generator = generator;
        _value = null;
    }
    
    public TerminalSymbol(String name, Functor generator) {
        this.names = new Vector<String>();
        this.names.add(name);
        this.generator = generator;
        _value = null;
    }
        
    public Object eval(DataNode symbolTable) {
        return _value;
    }
        
    public Symbol optimize() {
        return this;
    }
    
    public String toString() {
        if (this._value != null)
            return _value.toString();
        else
        {
            String result = names.get(0);
            for(int i=1;i<names.size();i++)
                result += " | " + names.get(i);
            return result;
        }
    }
    
    /** @brief copy ourself, and return new instance */
    public Symbol copy() {
        TerminalSymbol t = new TerminalSymbol(this.names, this.generator);
        return (Symbol)t;
    }
    
    public Type getType() { return Symbol.Type.TERMINAL; }
    
    public Functor getGenerator() {
        return generator;
    }
    
    public Object getValue() {
        return _value;
    }
    
    protected Object pick() {
        if (generator != null)
            return _value = names.get((Integer)generator.call() % names.size());
        else if (names.size() > 0)
            return _value = names.get(0);
        return null;
    }
    
    protected Vector<String> names;
    protected Object _value;
    protected Functor generator;
}
