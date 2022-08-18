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
 *   Richard Malek
 *   - Initial implementation
 *   David Omrai
 *   - Adding the JSP phenotype implementation
 *   - Fixing the compare method
 */

package org.seage.problem.fsp;

import org.seage.aal.problem.ProblemInfo;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.Schedule;

public class FspPhenotypeEvaluator extends JspPhenotypeEvaluator {
 
  /**
   * .
   * @param problemInfo .
   * @param jobsDefinition .
   * @throws Exception .
   */
  public FspPhenotypeEvaluator(
      ProblemInfo problemInfo, FspJobsDefinition jobsDefinition) throws Exception {
    super(problemInfo, jobsDefinition);
  }
      
  /**
   * Method evalueates the given phenotype with a number, makespan.
   * @param phenotype solution
   * @return double[] with one number representing the makespan
   * @throws Exception .
   */
  @Override
  public double[] evaluate(JspPhenotype phenotype) throws Exception {
    int jobsCount = jobsDefinition.getJobsCount();
    int machinesCount = jobsDefinition.getMachinesCount();
    Integer[] jobsArray = new Integer[jobsCount * machinesCount];

    for (int i = 0; i < jobsArray.length; i++) {
      jobsArray[i] = phenotype.getSolution()[i % jobsCount];
    }

    Schedule schedule = new Schedule(jobsDefinition, jobsArray);
    double makeSpan = schedule.getMakeSpan();
    double score = this.scoreCalculator.calculateInstanceScore(instanceID, makeSpan);
    return new double[] { makeSpan, score };
  }
}
