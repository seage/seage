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

package org.seage.aal.reporter;

import java.io.Serializable;
import java.util.Comparator;

/**
 *
 * @author Richard Malek
 */
public abstract class AlgorithmReportEvaluator implements Serializable, Comparator<AlgorithmReport> {

  /**
   * 
   */
  private static final long serialVersionUID = 7128431164404468045L;

  /**
   * Evaluates statistics of previous algorithm run
   * 
   * @param statistics
   * @return A number between 0 and 100 (0 is the best, 100 is the worst)
   */
  public abstract int evaluate(AlgorithmReport statistics);

  @Override
  public abstract int compare(AlgorithmReport o1, AlgorithmReport o2);
}
