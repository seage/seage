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

package org.seage.metaheuristic.genetics;

import java.io.Serializable;

/**
 * Gene implementation.
 * @author Richard Malek (original)
 */
public class Gene implements Serializable {
  private static final long serialVersionUID = -2263063124222499089L;
  protected Object value;

  private Gene() {
  }

  public Gene(Object value) {
    this.value = value;
  }

  @Override
  public String toString() {
    Integer i = Integer.parseInt(value.toString());
    return i.toString();
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }  

  @Override
  public boolean equals(Object value0) {
    return value.equals(value0);
  }

  @Override
  public Object clone() {
    Gene gene = new Gene();
    gene.value = value;
    return gene;
  }
}
