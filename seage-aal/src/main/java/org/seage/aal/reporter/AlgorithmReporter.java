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
package org.seage.aal.reporter;

import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.data.DataNode;

/**
 * Reports on runtime information
 *
 * @author Richard Malek
 */
public class AlgorithmReporter<P extends Phenotype<?>> {
  private AlgorithmReport _report;
  private IPhenotypeEvaluator<P> _evaluator;

  public AlgorithmReporter(IPhenotypeEvaluator<P> evaluator) throws Exception {
    _evaluator = evaluator;
    _report = new AlgorithmReport("AlgorithmReport");
    _report.putDataNode(new DataNode("Log"));
    _report.putDataNode(new DataNode("Statistics"));
  }

  public void putParameters(AlgorithmParams params) throws Exception {
    _report.putValue("created", System.currentTimeMillis());
    _report.putDataNode(params);
  }

  public void putNewSolution(long time, long iterNumber, P solution) throws Exception {
    // TODO: Too much data, figure out what could be computed here
    // DataNode newSol = new DataNode("NewSolution");
    // newSol.putValue("time", time);
    // newSol.putValue("iterNumber", iterNumber);
    // newSol.putValue("objVal", _evaluator.evaluate(solution)[0]);
    // newSol.putValue("solution", solution.toText());
    // newSol.putValue("hash", solution.computeHash());

    // _report.getDataNode("Log").putDataNode(newSol);
  }

  public void putStatistics(long numberOfIterationsDone, long numberOfNewSolutions, long lastImprovingIteration,
      double initObjVal, double avgObjVal, double bestObjVal) throws Exception {
    DataNode stats = _report.getDataNode("Statistics");
    stats.putValue("numberOfIter", numberOfIterationsDone);
    stats.putValue("numberOfNewSolutions", numberOfNewSolutions);
    stats.putValue("lastIterNumberNewSol", lastImprovingIteration);
    stats.putValue("initObjVal", initObjVal);
    stats.putValue("avgObjVal", avgObjVal);
    stats.putValue("bestObjVal", bestObjVal);
  }

  public AlgorithmReport getReport() throws Exception {
    return (AlgorithmReport) _report.clone();
  }
}
