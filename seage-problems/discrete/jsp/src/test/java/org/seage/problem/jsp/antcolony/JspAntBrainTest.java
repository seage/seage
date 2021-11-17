package org.seage.problem.jsp.antcolony;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

public class JspAntBrainTest {
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
  public void testCreatingAntBrain() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    JspGraph graph = new JspGraph(jobs, eval);

    assertNotEquals(null, graph);
  }

  @Test
  public void testUsingAntBrain() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    JspGraph graph = new JspGraph(jobs, eval);
    JspAntBrain brain = new JspAntBrain(graph, jobs);

    List<Node> visNodes = new ArrayList<>();
    HashSet<Node> avalNodes = brain.getAvailableNodes(visNodes);

    // Test first aval nodes
    assertEquals(3, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(101)));
    assertTrue(avalNodes.contains(graph.getNodes().get(201)));
    assertTrue(avalNodes.contains(graph.getNodes().get(301)));

    // Test with one oper on the path
    visNodes.add(graph.getNodes().get(101));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(3, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(102)));
    assertTrue(avalNodes.contains(graph.getNodes().get(201)));
    assertTrue(avalNodes.contains(graph.getNodes().get(301)));

    visNodes.add(graph.getNodes().get(102));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(3, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(103)));
    assertTrue(avalNodes.contains(graph.getNodes().get(201)));
    assertTrue(avalNodes.contains(graph.getNodes().get(301)));

    visNodes.add(graph.getNodes().get(103));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(2, avalNodes.size());    
    assertTrue(avalNodes.contains(graph.getNodes().get(201)));
    assertTrue(avalNodes.contains(graph.getNodes().get(301)));

    visNodes.add(graph.getNodes().get(201));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(2, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(202)));
    assertTrue(avalNodes.contains(graph.getNodes().get(301)));

    visNodes.add(graph.getNodes().get(202));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(2, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(203)));
    assertTrue(avalNodes.contains(graph.getNodes().get(301)));

    visNodes.add(graph.getNodes().get(203));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(1, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(301)));

    visNodes.add(graph.getNodes().get(301));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(1, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(302)));

    visNodes.add(graph.getNodes().get(302));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(1, avalNodes.size());
    assertTrue(avalNodes.contains(graph.getNodes().get(303)));

    visNodes.add(graph.getNodes().get(303));
    avalNodes = brain.getAvailableNodes(visNodes);
    assertEquals(0, avalNodes.size());
  }
}
