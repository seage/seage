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

/**
 * Contributors: Richard Malek - Initial implementation, David Omrai - Adapting to JSSP problem
 */
package org.seage.problem.jssp;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.seage.aal.algorithm.Phenotype;

public class JsspPhenotype extends Phenotype<Integer[]> {

  /**
   * Class constructor sets the solution
   * Solution is the integer array where each number represents
   * the job operation for one working machine
   * @param schedule Integer[] solution
   * @throws Exception In case of wrong input
   */
  public JsspPhenotype(Integer[] schedule) throws Exception {
    super(schedule);
  }

  @Override
  public String toText() {
    return Arrays.toString(solution);
  }

  @Override
  public void fromText(String text) {
    String stringArray = text.substring(1, text.length() - 1);
    solution = Arrays.asList(stringArray.split(","))
        .stream().map(s -> Integer.parseInt(s.strip()))
        .collect(Collectors.toList())
        .toArray(new Integer[0]);
  }
}
