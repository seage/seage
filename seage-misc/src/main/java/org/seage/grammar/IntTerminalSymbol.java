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
