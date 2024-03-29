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
 * SEAGE. If not, @see <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * .
 * Contributors: Richard Malek - Initial implementation
 */

package org.seage.problem.sat.antcolony;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

/**
 * .
 *
 * @author Zagy
 */
public class SatGraph extends Graph {

  /**
   * .
   *
   */
  public SatGraph(int literalsCount) {
    super();

    // /~ 1 ~ 2 ~ 3 ~ ... n
    // 0
    // \~ -1 ~ -2 ~ -3 ~ ... -n
    _nodes.put(0, new Node(0));

    for (int i = 1; i <= literalsCount; i++) {
      _nodes.put(i, new Node(i));
      _nodes.put(-i, new Node(-i));
    }
  }
}
