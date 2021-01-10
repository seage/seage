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

import java.util.Vector;

import org.seage.data.DataNode;

/**
 *
 * @author jenik
 */
public class TerminalSymbol implements Symbol {

  /**
   * 
   */
  private static final long serialVersionUID = 1649812001103974726L;

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

  @Override
  public Object eval(DataNode symbolTable) {
    return _value;
  }

  @Override
  public Symbol optimize() {
    return this;
  }

  @Override
  public String toString() {
    if (this._value != null)
      return _value.toString();
    else {
      String result = names.get(0);
      for (int i = 1; i < names.size(); i++)
        result += " | " + names.get(i);
      return result;
    }
  }

  /** @brief copy ourself, and return new instance */
  @Override
  public Symbol copy() {
    TerminalSymbol t = new TerminalSymbol(this.names, this.generator);
    return t;
  }

  @Override
  public Type getType() {
    return Symbol.Type.TERMINAL;
  }

  public Functor getGenerator() {
    return generator;
  }

  public Object getValue() {
    return _value;
  }

  protected Object pick() {
    if (generator != null)
      return _value = names.get((Integer) generator.call() % names.size());
    else if (names.size() > 0)
      return _value = names.get(0);
    return null;
  }

  protected Vector<String> names;
  protected Object _value;
  protected Functor generator;
}
