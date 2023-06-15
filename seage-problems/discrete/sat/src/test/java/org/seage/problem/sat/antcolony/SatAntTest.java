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

 * Contributors:
 *     Richard Malek
 *     - Initial implementation
 *     David Omrai
 *     - Implementation of tests
 */

package org.seage.problem.sat.antcolony;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.Edge;
import org.seage.metaheuristic.antcolony.Node;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;
import org.seage.problem.sat.FormulaReader;

/**
 * Class tests the method of SatAnt.
 */
public class SatAntTest {
  String path = "seage-problems/discrete/sat/src/main/resources/org/seage/problem/sat/test/uf04.cnf";

  @Test
  public void testGetAvailableNodes() throws Exception {
    Formula formula = new Formula(new ProblemInstanceInfo("", ProblemInstanceOrigin.FILE, path),
        FormulaReader.readClauses(new FileInputStream(path)));

    Graph graph = new SatGraph(formula, new FormulaEvaluator(formula));
    // The ant initially travels through two nodes, thus next two available nodes expected.
    // Ant ant = new Ant(graph.nodesToNodePath(List.of(1, 2)), 42);
    // ant.setParameters(1, 1, 20);

    // List<Edge> edgePath = ant.doFirstExploration(graph);
    // List<Node> nodePath = Graph.edgeListToNodeList(edgePath);

    // // It should return not yet visited nodes (the rest two)
    // HashSet<Node> availNodes = ant.getAvailableNodes(graph, nodePath);

    // assertFalse(availNodes.contains(graph.getNodes().get(1)));
    // assertFalse(availNodes.contains(graph.getNodes().get(2)));
    // assertTrue(availNodes.contains(graph.getNodes().get(3)));
    // assertTrue(availNodes.contains(graph.getNodes().get(4)));
  }

  @Test
  public void testGetNodeDistance() {
    assertTrue(false);
    // TODO
  }
}
