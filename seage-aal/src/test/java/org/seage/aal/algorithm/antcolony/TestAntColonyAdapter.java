package org.seage.aal.algorithm.antcolony;

import java.util.ArrayList;
import java.util.Arrays;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.Graph;

public class TestAntColonyAdapter extends AntColonyAdapter<TestPhenotype, Ant> {

  public TestAntColonyAdapter(Graph graph, IPhenotypeEvaluator<TestPhenotype> phenotypeEvaluator) {
    super(graph, phenotypeEvaluator);
  }

  @Override
  public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception {
    ants = new Ant[source.length];

    for (int i = 0; i < ants.length; i++) {
      ArrayList<Integer> nodes = new ArrayList<Integer>();
      nodes.addAll(Arrays.asList(source[i].getSolution()));

      ants[i] = new Ant(graph, nodes);
    }
  }

  @Override
  public TestPhenotype[] solutionsToPhenotype() throws Exception {
    TestPhenotype[] result = new TestPhenotype[this.ants.length];
    for (int i = 0; i < this.ants.length; i++) {
      result[i] = solutionToPhenotype(this.ants[i]);
    }
    return result;
  }

  @Override
  public TestPhenotype solutionToPhenotype(Ant solution) throws Exception {
    Integer[] p = (Integer[]) solution.getNodeIDsAlongPath().stream().toArray(Integer[]::new);
    return new TestPhenotype(p);
  }
}
