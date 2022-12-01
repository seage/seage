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

package org.seage.problem.tsp;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemScoreCalculator;

/**
 * TSP phenotype is a sequence of city IDs starting at _1_
 * 
 * @author Richard Malek
 */
public class TspPhenotypeEvaluator implements IPhenotypeEvaluator<TspPhenotype> {
  protected TspProblemInstance _instance;
  protected City[] _cities;
  private ProblemScoreCalculator scoreCalculator;

  public TspPhenotypeEvaluator(ProblemInfo problemInfo, TspProblemInstance instance) {
    super();
    _instance = instance;
    _cities = instance.getCities();
    this.scoreCalculator = new ProblemScoreCalculator(problemInfo);
  }

  @Override
  public double[] evaluate(TspPhenotype phenotype) throws Exception {
    double tourLength = 0;

    int numCities = _cities.length;

    for (int i = 0; i < numCities; i++) {
      int k = i + 1;
      if (i == numCities - 1) {
        k = 0;
      }

      int ix1 = phenotype.getSolution()[i] - 1;
      int ix2 = phenotype.getSolution()[k] - 1;
      double x1 = _cities[ix1].X;
      double y1 = _cities[ix1].Y;
      double x2 = _cities[ix2].X;
      double y2 = _cities[ix2].Y;
      tourLength += Math.round(Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)));
    }
    String instanceID = _instance.getProblemInstanceInfo().getInstanceID();
    return new double[] { 
      tourLength,
      this.scoreCalculator.calculateInstanceScore(instanceID, tourLength)
    };
  }

  @Override
  public int compare(double[] o1, double[] o2) {
    if (o1 == null) {
      return -1;
    }
    if (o2 == null) {
      return 1;
    }

    int length = o1.length <= o2.length ? o1.length : o2.length;

    for (int i = 0; i < length; i++) {
      if (o1[i] < o2[i]) {
        return 1;
      }
      if (o1[i] > o2[i]) {
        return -1;
      }
    }
    return 0;
  }
}
