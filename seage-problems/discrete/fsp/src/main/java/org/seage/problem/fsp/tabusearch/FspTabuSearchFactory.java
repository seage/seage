/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors
 * 
 * This file is part of SEAGE.
 * 
 * SEAGE is free software: you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * SEAGE is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation David Omrai - Following implementation
 */
package org.seage.problem.fsp.tabusearch;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.problem.fsp.FspPhenotypeEvaluator;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.tabusearch.JspTabuSearchAdapter;
import org.seage.problem.jsp.tabusearch.JspTabuSearchSolution;


/**
 *
 * @author Richard Malek
 */
@Annotations.AlgorithmId("TabuSearch")
@Annotations.AlgorithmName("Tabu Search")
public class FspTabuSearchFactory
    implements IAlgorithmFactory<JspPhenotype, JspTabuSearchSolution> {

  @Override
  public Class<?> getAlgorithmClass() {
    return TabuSearchAdapter.class;
  }

  @Override
  public IAlgorithmAdapter<JspPhenotype, JspTabuSearchSolution> createAlgorithm(
      ProblemInstance instance, IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator)
      throws Exception {
    return new JspTabuSearchAdapter(
        new FspMoveManager((JspJobsDefinition) instance),
        new FspObjectiveFunction((FspPhenotypeEvaluator) phenotypeEvaluator), phenotypeEvaluator);
  }
}
