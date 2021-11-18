package org.seage.problem.jsp.antcolony;


import java.io.InputStream;
import java.util.Arrays;
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
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.metaheuristic.antcolony.Ant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    JspAntBrain brain = new JspAntBrain(graph, jobs, eval);

    List<Node> visitedNodes = new ArrayList<>();
    HashSet<Node> availableNodes = brain.getAvailableNodes(visitedNodes);

    // Test first aval nodes
    assertEquals(3, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(101)));
    assertTrue(availableNodes.contains(graph.getNodes().get(201)));
    assertTrue(availableNodes.contains(graph.getNodes().get(301)));

    // Test with one oper on the path
    visitedNodes.add(graph.getNodes().get(101));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(3, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(102)));
    assertTrue(availableNodes.contains(graph.getNodes().get(201)));
    assertTrue(availableNodes.contains(graph.getNodes().get(301)));

    visitedNodes.add(graph.getNodes().get(102));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(3, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(103)));
    assertTrue(availableNodes.contains(graph.getNodes().get(201)));
    assertTrue(availableNodes.contains(graph.getNodes().get(301)));

    visitedNodes.add(graph.getNodes().get(103));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(2, availableNodes.size());    
    assertTrue(availableNodes.contains(graph.getNodes().get(201)));
    assertTrue(availableNodes.contains(graph.getNodes().get(301)));

    visitedNodes.add(graph.getNodes().get(201));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(2, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(202)));
    assertTrue(availableNodes.contains(graph.getNodes().get(301)));

    visitedNodes.add(graph.getNodes().get(202));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(2, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(203)));
    assertTrue(availableNodes.contains(graph.getNodes().get(301)));

    visitedNodes.add(graph.getNodes().get(203));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(1, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(301)));

    visitedNodes.add(graph.getNodes().get(301));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(1, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(302)));

    visitedNodes.add(graph.getNodes().get(302));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(1, availableNodes.size());
    assertTrue(availableNodes.contains(graph.getNodes().get(303)));

    visitedNodes.add(graph.getNodes().get(303));
    availableNodes = brain.getAvailableNodes(visitedNodes);
    assertEquals(0, availableNodes.size());
  }

  @Test
  public void testSelectNextStep() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    JspGraph graph = new JspGraph(jobs, eval);
    JspAntBrain brain = new JspAntBrain(graph, jobs, eval);


    List<Node> nodePath = new ArrayList<>();
    nodePath.add(graph.getNodes().get(0));
    assertNotNull(brain.selectNextStep(nodePath).getNode2());

    nodePath.add(graph.getNodes().get(101));
    nodePath.add(graph.getNodes().get(102));
    nodePath.add(graph.getNodes().get(103));

    nodePath.add(graph.getNodes().get(201));
    nodePath.add(graph.getNodes().get(202));
    nodePath.add(graph.getNodes().get(203));

    assertEquals(graph.getNodes().get(301), brain.selectNextStep(nodePath).getNode2());

    nodePath.add(graph.getNodes().get(301));

    assertEquals(graph.getNodes().get(302), brain.selectNextStep(nodePath).getNode2());

    nodePath.add(graph.getNodes().get(302));

    assertEquals(graph.getNodes().get(303), brain.selectNextStep(nodePath).getNode2());

    nodePath.add(graph.getNodes().get(303));

    assertNull(brain.selectNextStep(nodePath));
  }

  @Test
  public void testAntFirstExplorationWithInitPathNoEdges() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator eval = new JspPhenotypeEvaluator(pi, jobs);
    JspGraph graph = new JspGraph(jobs, eval);
    graph.setDefaultPheromone(1);
    
    JspAntBrain brain = new JspAntBrain(graph, jobs, eval);

    graph.getEdges().clear();
    Ant a = new Ant(brain, graph, Arrays.asList(0, 101, 201));
    List<Edge> edges = a.doFirstExploration();
    assertNotNull(edges);
    assertEquals(2, edges.size());
    assertEquals(2, graph.getEdges().size());
    assertTrue(graph.getEdges().containsAll(edges));
  }
}
