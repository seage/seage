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
 * along with SEAGE. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.seage.metaheuristic.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AntColonyTest {

  class TestBrain extends AntBrain {

    public TestBrain(Graph graph) {
      super(graph);
    }

    @Override
    public double getNodeDistance(List<Node> nodePath, Node node) {
      TestNode n1 = (TestNode)nodePath.get(nodePath.size()-1);
      TestNode n2 = (TestNode)node;

      return Math.sqrt(Math.pow(n1.x-n2.x, 2) + Math.pow(n1.y-n2.y, 2));
    }
    
    @Override
    protected HashSet<Node> getAvailableNodes(List<Node> nodePath) {
      var result = super.getAvailableNodes(nodePath);
      if (result.size() == 0 && nodePath.get(0) != nodePath.get(nodePath.size()-1)) {
        result.add(graph.getNodes().get(1));
      }
      
      return result;
    }

  }

  class TestNode extends Node{
    public double x;
    public double y;

    public TestNode(int id, double x, double y) {
      super(id);
      this.x = x;
      this.y = y;
    }  
  }

  private Graph createGraph() {
    Graph graph = new Graph();
    graph.getNodes().put(1, new TestNode(1,  1.0,  0.0));
    graph.getNodes().put(2, new TestNode(2,  0.0, -1.0));
    graph.getNodes().put(3, new TestNode(3, -1.0,  0.0));
    graph.getNodes().put(4, new TestNode(4,  0.0,  1.0));
    return graph;
  }

  @Test
  public void testAuxClasses() {
    Graph graph = createGraph();
    AntBrain brain = new TestBrain(graph);

    var nodes = graph.getNodes();
    double d = 0.0;
    d = brain.getNodeDistance(Arrays.asList(nodes.get(1)), nodes.get(2));
    assertEquals(1.41, d, 0.005);
    d = brain.getNodeDistance(Arrays.asList(nodes.get(1)), nodes.get(3));
    assertEquals(2.0, d, 0.005);
  }

  @Test
  public void testProblem() throws Exception {
    Graph graph = createGraph();
    AntColony colony = new AntColony(graph);
    AntBrain brain = new TestBrain(graph);
    Ant ant = new Ant(brain, graph, null);
    colony.setParameters(1, 1, 1, 1, 1, 1);
    colony.startExploring(graph.getNodes().get(1), new Ant[] {ant});
    assertTrue(true);
  }
}
