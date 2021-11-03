package org.seage.aal.algorithm.sannealing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

public class TestSimulatedAnnealingAdapter extends SimulatedAnnealingAdapter<TestPhenotype, TestSolution> {

  public TestSimulatedAnnealingAdapter(Solution initialSolution, IObjectiveFunction objectiveFunction,
      IMoveManager moveManager, IPhenotypeEvaluator<TestPhenotype> phenotypeEvaluator, boolean maximizing)
      throws Exception {
    super(objectiveFunction, moveManager, phenotypeEvaluator, maximizing);
  }

  @Override
  public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception {
    this.solutions = new ArrayList<TestSolution>(source.length);

    for (int i = 0; i < source.length; i++) {
      TestSolution s = new TestSolution(source[i].getSolution());
      this.solutions.set(i,s);
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
    return new TestPhenotype(solution.solution);
  }

}
