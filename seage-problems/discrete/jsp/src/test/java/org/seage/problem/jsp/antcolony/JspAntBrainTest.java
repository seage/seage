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
  public void testAvailableNodes() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    JspGraph graph = new JspGraph(jobs, eval);
    JspAntBrain brain = new JspAntBrain(graph, jobs);

    List<Node> visitedNodes = new ArrayList<>();
    HashSet<Node> availableNodes = brain.getAvailableNodes(visitedNodes);

    // Test first aval nodes
    assertEquals(3, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(11)));
    assertTrue(availableNodes.contains(graph.getNodes().get(21)));
    assertTrue(availableNodes.contains(graph.getNodes().get(31)));

    // Test with one oper on the path
    visitedNodes.add(graph.getNodes().get(11));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(3, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(12)));
    assertTrue(availableNodes.contains(graph.getNodes().get(21)));
    assertTrue(availableNodes.contains(graph.getNodes().get(31)));

    visitedNodes.add(graph.getNodes().get(12));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(3, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(13)));
    assertTrue(availableNodes.contains(graph.getNodes().get(21)));
    assertTrue(availableNodes.contains(graph.getNodes().get(31)));

    visitedNodes.add(graph.getNodes().get(13));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(2, availableNodes.size());    
    assertTrue(availableNodes.contains(graph.getNodes().get(21)));
    assertTrue(availableNodes.contains(graph.getNodes().get(31)));

    visitedNodes.add(graph.getNodes().get(21));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(2, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(22)));
    assertTrue(availableNodes.contains(graph.getNodes().get(31)));

    visitedNodes.add(graph.getNodes().get(22));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(2, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(23)));
    assertTrue(availableNodes.contains(graph.getNodes().get(31)));

    visitedNodes.add(graph.getNodes().get(23));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(1, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(31)));

    visitedNodes.add(graph.getNodes().get(31));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(1, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(32)));

    visitedNodes.add(graph.getNodes().get(32));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(1, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(33)));

    visitedNodes.add(graph.getNodes().get(33));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(0, availableNodes.size());
  }
}
