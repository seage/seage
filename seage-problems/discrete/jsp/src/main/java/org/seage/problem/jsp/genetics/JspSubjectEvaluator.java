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
package org.seage.problem.jsp.genetics;

import org.seage.metaheuristic.genetics.Subject;
import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 * Summary description for JspSubjectEvaluator.
 * @author Richard Malek
 */
public class JspSubjectEvaluator extends SubjectEvaluator<Subject<Integer>>
{
  private JspPhenotypeEvaluator _phenotypeEvaluator;

  public JspSubjectEvaluator(JspPhenotypeEvaluator phenotypeEvaluator)
  {
    _phenotypeEvaluator = phenotypeEvaluator;
  }

  @Override
  public double[] evaluate(Subject<Integer> solution) throws Exception {
    return _phenotypeEvaluator.evaluate(new JspPhenotype(solution.getChromosome().getGenes()));
  }
}
