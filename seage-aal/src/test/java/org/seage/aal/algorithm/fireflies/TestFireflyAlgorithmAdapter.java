package org.seage.aal.algorithm.fireflies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;

public class TestFireflyAlgorithmAdapter 
    extends FireflyAlgorithmAdapter<TestPhenotype, TestSolution> {

  public TestFireflyAlgorithmAdapter(FireflyOperator operator, ObjectiveFunction evaluator,
      IPhenotypeEvaluator<TestPhenotype> phenotypeEvaluator, boolean maximizing) {
    super(operator, evaluator, phenotypeEvaluator, maximizing);
  }

  @Override
  public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception {
    this.solutions = new ArrayList<TestSolution>(source.length);

    for (int i = 0; i < source.length; i++) {
      TestSolution s = new TestSolution(source[i].getSolution());
      this.solutions.add(s);
    }
  }

  @Override
  public TestPhenotype[] solutionsToPhenotype() throws Exception {
    TestPhenotype[] result = new TestPhenotype[this.solutions.size()];
    for (int i = 0; i < this.solutions.size(); i++) {
      result[i] = solutionToPhenotype(this.solutions.get(i));
    }
    return result;
  }

  @Override
  public TestPhenotype solutionToPhenotype(TestSolution solution) throws Exception {
    return new TestPhenotype((Integer[])solution.solution);
  }

}
