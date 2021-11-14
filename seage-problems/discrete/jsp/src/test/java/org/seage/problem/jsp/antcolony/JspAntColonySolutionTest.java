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
 *   David Omrai
 *   - Test implementation
 */
package org.seage.problem.jsp.antcolony;

import java.io.InputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;
import org.seage.metaheuristic.antcolony.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class JspAntColonySolutionTest {
  static JobsDefinition jobs;

  @BeforeAll
  public static void prepareData() {
    try {
      String instanceID = "yn_3x3_example";
      String path = String.format("/org/seage/problem/jsp/test-instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      jobs = null;

      try(InputStream stream = JspAntColonySolutionTest.class.getResourceAsStream(path)) {
        jobs = new JobsDefinition(jobInfo, stream);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testProblem() {
    assertTrue(true);
  }

  @Test
  public void testCreatingSolution() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    JspAntColonySolution graph = new JspAntColonySolution(jobs, eval);

    assertNotEquals(null, graph);
  }

  @Test
  public void testGetNodeDistance() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    JspAntColonySolution graph = new JspAntColonySolution(jobs, eval);

    int factor = 0;
    for (int i = 0; i < jobs.getJobInfos().length; i++){
      int curOper = jobs.getJobInfos()[i].getOperationInfos().length;
      if (curOper > factor)
        factor = curOper;
    }

    // Test node ids
    assertEquals(0, graph.getNodes().get(0).getID());
    assertEquals(factor * jobs.getJobsCount() + 1, graph.getNodes().size());

    // Test the node values (jobid * factor + oper id) + starting node
    Node nd = graph.getNodes().get((2 * factor + 1) + 1);
    assertEquals(2, graph.nodeToJobID(nd));
    assertEquals(1, graph.nodeToOperID(nd));
  }
}
