package org.seage.problem.sat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemMetadataGenerator;

public class SatProblemMetadataGenerator
    extends ProblemMetadataGenerator<SatPhenotype> {

  public SatProblemMetadataGenerator(SatProblemProvider problemProvider) {
    super(problemProvider);
  }

  @Override
  protected Map<String, Double> getOptimalValues() throws Exception {
    HashMap<String, Double> result = new HashMap<String, Double>();
    List<ProblemInstanceInfo> instances = problemProvider.getProblemInfo().getProblemInstanceInfos();
    for(ProblemInstanceInfo pii : instances) {
      result.put(pii.getInstanceID(), 0.0);
    }
    return result;
  }

  @Override
  protected double generateRandomResult(ProblemInstance instance,
      IPhenotypeEvaluator<SatPhenotype> evaluator) throws Exception {
    return SatInitialSolutionProvider
        .generateRandomSolution((Formula) instance, evaluator, System.currentTimeMillis())
        .getObjValue();
  }

  @Override
  protected double generateGreedyResult(ProblemInstance instance,
      IPhenotypeEvaluator<SatPhenotype> evaluator) throws Exception {
    return SatInitialSolutionProvider
        .generateGreedySolution((Formula) instance, evaluator, System.currentTimeMillis())
        .getObjValue();
  }
}
