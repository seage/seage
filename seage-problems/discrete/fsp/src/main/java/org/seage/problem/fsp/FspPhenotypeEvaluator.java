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
 *   - Adding the FSP phenotype implementation
 */

package org.seage.problem.fsp;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemScoreCalculator;


public class FspPhenotypeEvaluator implements IPhenotypeEvaluator<FspPhenotype> {
  private String instanceID;
  private FspJobsDefinition jobsDefinition;
  private ProblemScoreCalculator scoreCalculator;

  /**
   * .
   * @param problemInfo .
   * @param jobsDefinition .
   * @throws Exception .
   */
  public FspPhenotypeEvaluator(
      ProblemInfo problemInfo, FspJobsDefinition jobsDefinition
  ) throws Exception {
    this.jobsDefinition = jobsDefinition;
    this. instanceID = jobsDefinition.getProblemInstanceInfo().getInstanceID();

    this.scoreCalculator = new ProblemScoreCalculator(problemInfo);
  }

  /**
   * Method evalueates the given phenotype with a number, makespan.
   * @param phenotype solution
   * @return double[] with one number representing the makespan
   * @throws Exception .
   */
  @Override
  public double[] evaluate(FspPhenotype phenotype) throws Exception {
    FspSchedule schedule = new FspSchedule(jobsDefinition, phenotype.getSolution());
    double makeSpan = schedule.getMakeSpan();
    double score = this.scoreCalculator.calculateInstanceScore(instanceID, makeSpan);
    return new double[] { makeSpan, score };
  }
      
  @Override
  public int compare(double[] arg0, double[] arg1) {
    return (int)(arg1[0] - arg0[0]);
  }

  public double evaluateSchedule(Integer[] jobArray) throws Exception {
    FspSchedule schedule = new FspSchedule(jobsDefinition, jobArray);
    return schedule.getMakeSpan();
  }
}