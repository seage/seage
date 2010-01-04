/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ailibrary.grammar;

import java.util.*;
import java.math.*;

/**
 *
 * @author jenik
 */
public class IntTerminalSymbol extends TerminalSymbol {
    
    public IntTerminalSymbol(Vector<String> names, int id) {
        super(names, new IntGeneratorFunctor(id));
        //value = -1;
    }
    
    public IntTerminalSymbol(String name, int id) {
        super(name, new IntGeneratorFunctor(id));
        //value = -1;
    }
    
    public IntTerminalSymbol(IntTerminalSymbol other) {
        super(other.names, other.generator);
        low = other.low;
        high = other.high;
    }
    
    /** @brief sets range of allowed values of this terminal */
    public void setDomain(int low, int high) {
        this.low = low;
        this.high = high;
    }
    
    public Object eval(HashMap symbolTable) {
        return pick();
    }

    
    /** @brief picks integer constant */
    public Integer pick() {
        Integer result = (Integer)generator.call();
        if (result < 0)
            result *= -1;
        result %= (high - low);
        result += low;
        _value = result;
        return (int)result;
    }
    
    public Integer getValue() {
        return (Integer)_value;
    }
    
    
    /** @brief copy ourself, and return new instance */
    public Symbol copy() {
        return (Symbol) new IntTerminalSymbol(this);
    }


    private int low;
    private int high;
    
}
