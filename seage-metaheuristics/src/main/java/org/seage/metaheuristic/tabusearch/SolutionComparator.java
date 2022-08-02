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
 *     Robert Harder
 *     - Initial implementation
 *     Richard Malek
 *     - Merge with SEAGE
 */
package org.seage.metaheuristic.tabusearch;

import java.util.Comparator;

/**
 * SolutionComparator.
 *
 * @author Richard Malek
 */
public class SolutionComparator implements Comparator<Object> {
  private boolean _maximizing;

  public SolutionComparator(boolean maximizing) {
    _maximizing = maximizing;
  }

  @Override
  public int compare(Object o1, Object o2) {
    Solution s1 = (Solution) o1;
    Solution s2 = (Solution) o2;
    // boolean b =
    // SingleThreadedTabuSearch.firstIsBetterThanSecond(s1.getObjectiveValue(),
    // s2.getObjectiveValue(), _maximizing);

    // if (b == true)
    // return 1;
    // else
    // return -1;

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
    int max = -1;
    if (_maximizing == false)
      max = 1;

    boolean b1 = d1 > d2;
    boolean b2 = d1 < d2;

    if (b1 == false && b2 == false)
      return 0;
    else if (b1 == true)
      return max;
    else if (b2 == true)
      return -max;
    return 0;
  }
}
