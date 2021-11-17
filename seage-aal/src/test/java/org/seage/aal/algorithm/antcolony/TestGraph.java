package org.seage.aal.algorithm.antcolony;

import java.util.List;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

public class TestGraph extends Graph {

  @Override
  public double getNodeDistance(List<Node> nodePath, Node node) {
    return 1;
  }

}
