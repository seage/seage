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
package org.seage.problem.jssp.genetics;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.problem.jssp.JsspPhenotype;
import org.seage.problem.jssp.JsspPhenotypeEvaluator;

/**
 * Summary description for JSSPGSEvaluator.
 * @author Richard Malek
 */
public class JsspSubjectEvaluator extends SubjectEvaluator<Subject<Integer>>
{
  private IPhenotypeEvaluator<JsspPhenotype> _phenotypeEvaluator;

  public JsspSubjectEvaluator(IPhenotypeEvaluator<JsspPhenotype> phenotypeEvaluator) {
    _phenotypeEvaluator = phenotypeEvaluator;
  }

  public JsspSubjectEvaluator(JsspPhenotypeEvaluator phenotypeEvaluator)
  {
    _phenotypeEvaluator = phenotypeEvaluator;
  }

  @Override
  public double[] evaluate(Subject<Integer> solution) throws Exception {
    return _phenotypeEvaluator.evaluate(new JsspPhenotype(solution.getChromosome().getGenes()));
  }

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
