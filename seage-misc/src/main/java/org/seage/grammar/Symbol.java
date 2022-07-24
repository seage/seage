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
 * along with SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 */

package org.seage.grammar;

import java.io.Serializable;

import org.seage.data.DataNode;

/**
 *
 * @author jenik
 */
public interface Symbol extends Serializable {

  public enum Type {
    TERMINAL, NONTERMINAL
  };

  /**
   * @brief evaluate derivate tree
   * @param symbolTable table of symbols and their values
   * @retval result of the operation(s)
   */
  abstract public Object eval(DataNode symbolTable);

  /** @brief optimize derivate tree, return new derivate (sub)tree */
  abstract public Symbol optimize();

  /** @brief gets type of this symbol (termina, nonterminal) */
  abstract public Type getType();

  /** @brief copy ourself, and return new instance */
  abstract public Symbol copy();

}
