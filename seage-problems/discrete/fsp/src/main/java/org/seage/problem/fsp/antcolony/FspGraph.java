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
 * You should have received a copy of the GNU General Public License along with SEAGE. If not, @see
 * <a href="http://www.gnu.org/licenses/">http://www.gnu.org/licenses/</a>.
 *
 */

/**
 * Contributors: Richard Malek - Initial implementation David Omrai - Jsp implementation
 */

package org.seage.problem.fsp.antcolony;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 * .
 * 
 * @author Zagy
 * @author David Omrai
 */
public class FspGraph extends Graph {
  JspPhenotypeEvaluator evaluator;

  private JspJobsDefinition jobs;

  /**
   * .
   * @param jobArray .
   * @param evaluator .
   * @throws Exception .
   */
  public FspGraph(JspJobsDefinition jobArray, JspPhenotypeEvaluator evaluator) throws Exception {
    super();
    this.evaluator = evaluator;
    this.jobs = jobArray;

    // Starting node
    _nodes.put(0, new Node(0));

    for (int jobId = 1; jobId <= jobs.getJobsCount(); jobId++) {
      _nodes.put(jobId, new Node(jobId));
    }
  }
}
