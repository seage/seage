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

package org.seage.problem.tsp.genetics;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspPhenotype;
import org.seage.problem.tsp.TspPhenotypeEvaluator;

/**
 *
 * @author Richard Malek
 */
public class TspEvaluator extends SubjectEvaluator<Subject<Integer>> {
  private IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator;

  public TspEvaluator(IPhenotypeEvaluator<TspPhenotype> phenotypeEvaluator) {
    this.phenotypeEvaluator = phenotypeEvaluator;
  }

  @Override
  public double[] evaluate(Subject<Integer> solution) throws Exception {
    return phenotypeEvaluator.evaluate(new TspPhenotype(solution.getChromosome().getGenes()));
  }

  // private double[] evaluate(Integer[] phenotypeSubject) throws Exception
  // {
  // double tourLength = 0;
  // int numCities = _cities.length;
  //
  // for (int i = 0; i < numCities; i++)
  // {
  // int k = i + 1;
  // if (i == numCities - 1)
  // k = 0;
  //
  // int ix1 = (Integer)phenotypeSubject[i];
  // int ix2 = (Integer)phenotypeSubject[k];
  // double x1 = _cities[ix1].X;
  // double y1 = _cities[ix1].Y;
  // double x2 = _cities[ix2].X;
  // double y2 = _cities[ix2].Y;
  // tourLength += Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  // }
  // return new double[] {tourLength };
  // }

  public int compare(double[] o1, double[] o2) {
    if (o1 == null)
      return -1;
    if (o2 == null)
      return 1;

    int length = o1.length <= o2.length ? o1.length : o2.length;

    for (int i = 0; i < length; i++) {
      if (o1[i] < o2[i])
        return 1;
      if (o1[i] > o2[i])
        return -1;
    }
    return 0;
  }
}
