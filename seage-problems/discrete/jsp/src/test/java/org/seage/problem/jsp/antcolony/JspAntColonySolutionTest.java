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
import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;

import org.seage.aal.problem.ProblemProviderTestBase;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@Disabled
public class JspAntColonySolutionTest {

  @Test
  public void testProblem() {
    assertTrue(false);
  }

  // public static void main(String[] args) {
  //   try {
  //     String instanceID = "ft10";
  //     String path = String.format("/org/seage/problem/jsp/instances/%s.xml", instanceID);
  //     ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
  //     JobsDefinition jobs = null;

  //     try(InputStream stream = JspAntColonySolutionTest.class.getResourceAsStream(path)) {
  //       jobs = new JobsDefinition(jobInfo, stream);
  //     }

  //     new JspAntColonySolutionTest().testSolution(jobs);
  //   } catch (Exception e) {
  //     e.printStackTrace();
  //   }
  // }

  // /**
  //  * Method tests the creation of jsp ant soluiton
  //  * @param jobs
  //  */
  // public void testSolution(JobsDefinition jobs) throws Exception {
  //   JspProblemProvider problemProvider = new JspProblemProvider();
  //   ProblemInfo pi = problemProvider.getProblemInfo();
  //   JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

  //   JspAntColonySolution sol = new JspAntColonySolution(jobs, eval);
  // }
}
