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
 * Contributors: Jan Zmatlik - Initial implementation
 */

package org.seage.problem.sat.sannealing;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInfo;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.SatPhenotype;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 * SatObjectiveFunction class.
 * @author Richard Malek
 */
public class SatObjectiveFunction implements IObjectiveFunction {
  private IPhenotypeEvaluator<SatPhenotype> evaluator;

  public SatObjectiveFunction(IPhenotypeEvaluator<SatPhenotype> evaluator) {
    this.evaluator = evaluator;
  }

  @Override
  public double getObjectiveValue(Solution solution) throws Exception {
    SatSolution sol = (SatSolution) solution;
    SatPhenotype phenotype = new SatPhenotype(sol.getLiteralValues());
    return evaluator.evaluate(phenotype)[0];
  }
}
