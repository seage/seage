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
package org.seage.metaheuristic.genetics;

/**
 * @author Richard Malek (original)
 */
public class SubjectComparator<S extends Subject<?>> implements java.util.Comparator<S> {
  @Override
  public int compare(S s1, S s2) {
    for (int i = 0; i < s1.getObjectiveValue().length; i++) {
      int result = compare(s1.getObjectiveValue()[i], s2.getObjectiveValue()[i]);
      if (result == 0)
        continue;
      else
        return result;

    }
    return 0;
  }

  private int compare(double d1, double d2) {
    // if (_maximizing)
    if (d1 < d2)
      return -1;
    if (d1 > d2)
      return 1;
    return 0;
  }

}
