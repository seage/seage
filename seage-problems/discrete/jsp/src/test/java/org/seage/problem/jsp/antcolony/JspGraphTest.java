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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspProblemProvider;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class JspGraphTest {
  static JobsDefinition jobs;

  @BeforeAll
  public static void prepareData() {
    try {
      String instanceID = "yn_3x3_example";
      String path = String.format("/org/seage/problem/jsp/test-instances/%s.xml", instanceID);
      ProblemInstanceInfo jobInfo = new ProblemInstanceInfo(instanceID, ProblemInstanceOrigin.RESOURCE, path);
      jobs = null;

      try(InputStream stream = JspGraphTest.class.getResourceAsStream(path)) {
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

    JspGraph graph = new JspGraph(jobs, eval);

    assertNotEquals(null, graph);
  }

  @Test
  public void testNodesIDs() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    JspGraph graph = new JspGraph(jobs, eval);

    int operSum = 0;
    for (int i = 0; i < jobs.getJobInfos().length; i++){
      operSum += jobs.getJobInfos()[i].getOperationInfos().length;
    }

    // Test node ids
    assertEquals(0, graph.getNodes().get(0).getID());
    assertEquals(operSum + 1, graph.getNodes().size());

    // Test the node values (jobid * factor + oper id)
    Node nd = graph.getNodes().get((2 * graph.getFactor() + 1));
    assertEquals(2, graph.nodeToJobID(nd));
    assertEquals(1, graph.nodeToOperID(nd));
  }

  @Test
  public void testGetNodeDistance() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);

    JspGraph graph = new JspGraph(jobs, eval);
    
    HashMap<Integer, Node> hm = graph.getNodes();

    List<Node> nodesPath = new ArrayList<>();
    nodesPath.add(hm.get(0));

    // Test start to first node
    assertEquals(1.0, graph.getNodeDistance(nodesPath, hm.get(101)));
    
    // Test the longer path
    Integer[] jobArray1 = new Integer[] {1};
    Integer[] jobArray2 = new Integer[] {1, 2};

    hm.get(0).addEdge(new Edge(hm.get(101), hm.get(0)));
    hm.get(101).addEdge(new Edge(hm.get(0), hm.get(101)));

    nodesPath.add(hm.get(101));

    assertEquals(
      eval.evaluateSchedule(jobArray2) - eval.evaluateSchedule(jobArray1) + 1, 
      graph.getNodeDistance(nodesPath, hm.get(201))
    );
    
    // test the three opers 
    Integer[] jobArray3 = new Integer[] {1, 2, 3};

    hm.get(101).addEdge(new Edge(hm.get(201), hm.get(101)));
    hm.get(201).addEdge(new Edge(hm.get(101), hm.get(201)));

    nodesPath.add(hm.get(201));

    assertEquals(
      eval.evaluateSchedule(jobArray3) - eval.evaluateSchedule(jobArray2) + 1,
      graph.getNodeDistance(nodesPath, hm.get(301))
    );

    // another test
    Integer[] jobArray4 = new Integer[] {1, 2, 3, 1};

    hm.get(201).addEdge(new Edge(hm.get(301), hm.get(201)));
    hm.get(301).addEdge(new Edge(hm.get(201), hm.get(301)));

    nodesPath.add(hm.get(301));

    assertEquals(
      eval.evaluateSchedule(jobArray4) - eval.evaluateSchedule(jobArray3) + 1,
      graph.getNodeDistance(nodesPath, hm.get(102))
    );
  }
}
