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

package org.seage.aal.algorithm.genetics;

import org.seage.aal.reporter.AlgorithmReport;
import org.seage.aal.reporter.AlgorithmReportEvaluator;

/**
 *
 * @author Richard Malek
 */
public class GeneticAlgorithmRuntimeEvaluator extends AlgorithmReportEvaluator {

  /**
   * 
   */
  private static final long serialVersionUID = -2908274605434683758L;

  @Override
  public int evaluate(AlgorithmReport report) {
    // InputStream is =
    // AlgorithmAdapter.class.getResourceAsStream("reportSchema.xsd");
    try {
      return Math.max(report.getDataNode("statistics").getValueInt("numberOfNewSolutions") - 5, 0);
    } catch (Exception ex) {
      return 0;
    }
  }

  @Override
  public int compare(AlgorithmReport o1, AlgorithmReport o2) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
