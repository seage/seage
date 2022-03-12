package org.seage.problem.tsp;

import java.util.Map;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemMetadataGenerator;

public class TspProblemMetadataGenerator extends ProblemMetadataGenerator<TspPhenotype>{

  public TspProblemMetadataGenerator(TspProblemProvider tspProblemProvider) {}

  @Override
  protected double generateRandomResult(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> evaluator) throws Exception {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected double generateGreedyResult(ProblemInstance instance,
      IPhenotypeEvaluator<TspPhenotype> evaluator) throws Exception {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected Map<String, Double> getOptimalValues() {
    // TODO Auto-generated method stub
    return null;
  }

}
