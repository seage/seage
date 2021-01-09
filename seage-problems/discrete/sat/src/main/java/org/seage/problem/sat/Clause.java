/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.seage.problem.sat;

/**
 * Summary description for Clause.
 */
public class Clause {

  private Literal[] _literals;

  public Clause(Literal[] literals) {
    _literals = literals;
  }

  public Literal[] getLiterals() {
    return _literals;
  }

  public void setLiterals(Literal[] literals) {
    _literals = literals;
  }

  @Override
  public String toString() {
    String result = "";

    for (int i = 0; i < _literals.length; i++) {
      result += _literals[i].toString() + "\t";
    }
    return result;
  }
}
