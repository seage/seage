package org.seage.aal.algorithm.antcolony;

import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

public class TestGraph extends Graph {

  @Override
  public double getNodesDistance(Node n1, Node n2) {
    return 1;
  }

}
