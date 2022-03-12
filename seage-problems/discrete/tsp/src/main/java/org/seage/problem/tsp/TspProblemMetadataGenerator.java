package org.seage.problem.tsp;

import java.util.Map;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemMetadataGenerator;

public class TspProblemMetadataGenerator extends ProblemMetadataGenerator<TspPhenotype> {

  public TspProblemMetadataGenerator(TspProblemProvider tspProblemProvider) {
    super(tspProblemProvider);
  }

  @Override
  protected Map<String, Double> getOptimalValues() throws Exception {
    return getOptimalValues("/org/seage/problem/tsp/solutions/__optimal.txt");
  }

  @Override
  protected double generateRandomResult(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> evaluator) throws Exception {
    return evaluator.evaluate(new TspPhenotype(TourProvider.createGreedyTour(
        ((TspProblemInstance) instance).getCities(), System.currentTimeMillis())))[0];
  }

  @Override
  protected double generateGreedyResult(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> evaluator) throws Exception {
    return evaluator.evaluate(new TspPhenotype(
        TourProvider.createRandomTour(((TspProblemInstance) instance).getCities().length)))[0];
  }
}
