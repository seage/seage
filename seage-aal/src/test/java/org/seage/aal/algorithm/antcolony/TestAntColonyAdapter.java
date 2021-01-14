package org.seage.aal.algorithm.antcolony;

import java.util.ArrayList;
import java.util.Arrays;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;

public class TestAntColonyAdapter extends AntColonyAdapter<TestPhenotype, Ant<AntBrain>> {

  public TestAntColonyAdapter(AntBrain brain, Graph graph, IPhenotypeEvaluator<TestPhenotype> phenotypeEvaluator) {
    super(brain, graph, phenotypeEvaluator);
  }

  @Override
  public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception {
    _ants = new Ant[source.length];

    for (int i = 0; i < _ants.length; i++) {
      ArrayList<Integer> nodes = new ArrayList<Integer>();
      nodes.addAll(Arrays.asList(source[i].getSolution()));

      _ants[i] = new Ant<>(nodes);
    }
  }

  @Override
  public TestPhenotype[] solutionsToPhenotype() throws Exception {
    TestPhenotype[] result = new TestPhenotype[this._ants.length];
    for (int i = 0; i < this._ants.length; i++) {
      result[i] = solutionToPhenotype(this._ants[i]);
    }
    return result;
  }

  @Override
  public TestPhenotype solutionToPhenotype(Ant<AntBrain> solution) throws Exception {
    Integer[] p = (Integer[]) solution.getNodeIDsAlongPath().stream().toArray(Integer[]::new);
    return new TestPhenotype(p);
  }
}
