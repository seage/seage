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
 *   Jan Zmatlik
 *   - Initial implementation
 */
package org.seage.problem.jsp.sannealing;

import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 *
 * @author Jan Zmatlik
 */
public class JspObjectiveFunction implements IObjectiveFunction
{
  private JspPhenotypeEvaluator _evaluator;
  public JspObjectiveFunction(JspPhenotypeEvaluator evaluator)
  {
    _evaluator = evaluator;
  }

  public double[] evaluate(JspSimulatedAnnealingSolution solution, int[] move) throws Exception
  {
    // For the moving operation
    int op1 = 0;
    int op2 = 0;
    Integer[] jobArray = solution.getJobArray();

    if (move != null) {
      // Store the operations ids
      op1 = jobArray[move[0]];
      op2 = jobArray[move[1]];

      // Swap the operations
      jobArray[move[0]] = op2;
      jobArray[move[1]] = op1;
    }
    
    double[] vlaues = _evaluator.evaluate(new JspPhenotype(jobArray));

    if (move != null) {
      // Restore the solution
      jobArray[move[0]] = op1;
      jobArray[move[1]] = op2;
    }

    return vlaues;    
  }

  @Override
  public double getObjectiveValue(Solution s) throws Exception
  {
    return evaluate((JspSimulatedAnnealingSolution) s, null)[0];
  }

}
