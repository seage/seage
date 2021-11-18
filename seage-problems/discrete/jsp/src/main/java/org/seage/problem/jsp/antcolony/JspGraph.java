/*******************************************************************************
 * Copyright (c) 2009 Richard Malek and SEAGE contributors

 * This file is part of SEAGE.

 * SEAGE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * SEAGE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */

/**
 * Contributors:
 *   Richard Malek
 *   - Initial implementation
 *   David Omrai
 *   - Jsp implementation
 */
package org.seage.problem.jsp.antcolony;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

/**
 *
 * @author Zagy
 * Edited by David Omrai
 */
public class JspGraph extends Graph
{
  JspPhenotypeEvaluator evaluator;

  private int factor;

  private JobsDefinition jobs;

  public JspGraph(JobsDefinition jobArary, JspPhenotypeEvaluator evaluator) throws Exception
  {
    super();
    this.evaluator = evaluator;
    this.jobs = jobArary;
    int allOperCount = 0;
    for (int i = 0; i < this.jobs.getJobsCount(); i++)
      allOperCount += this.jobs.getJobInfos()[i].getOperationInfos().length;

    this.factor = (int) Math.pow(10, Math.ceil(Math.log10(allOperCount))+1);

    // Add the first starting node
    //   /- 1 - 2 - 3 - ... - job1-oper '|
    //  /-  1 - 2 - 3 - ... - job2-oper  |
    // s -  1 - 2 - 3 - ... - job3-oper  | n-jobs
    //  \-  1 - 2 - 3 - ... - job4-oper  |
    //   \- 1 - 2 - 3 - ... - job5-oper ,|
    int id = 0;
    // Starting node
    _nodes.put(id, new Node(id));

    for (int idJob = 1; idJob <= jobs.getJobsCount(); idJob++) {
      for (int idOper = 1; idOper <= jobs.getJobInfos()[idJob-1].getOperationInfos().length; idOper++ ){
        id = idJob * getFactor() + idOper;
        _nodes.put(id, new Node(id));
      }
    }
  }

  public int nodeToJobID(Node node) {
    return node.getID() / getFactor();
  }

  public int nodeToOperID(Node node) {
    return node.getID() % getFactor();
  }

  public int getFactor() {
    return this.factor;
  }
}
