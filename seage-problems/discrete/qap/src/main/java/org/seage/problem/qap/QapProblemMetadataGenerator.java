package org.seage.problem.qap;

import java.util.Map;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemMetadataGenerator;
import org.seage.aal.problem.ProblemProvider;

public class QapProblemMetadataGenerator extends ProblemMetadataGenerator<QapPhenotype> {
  public QapProblemMetadataGenerator(ProblemProvider<QapPhenotype> problemProvider) {
    super(problemProvider);
  }

  @Override
  protected double generateRandomSolutionValue(ProblemInstance instance,
      IPhenotypeEvaluator<QapPhenotype> evaluator) throws Exception {
    QapProblemInstance pi = (QapProblemInstance) instance;
    Integer[] greedy = AssignmentProvider.createRandomAssignment(pi.getFacilityLocation());
    return evaluator.evaluate(new QapPhenotype(greedy))[0];
  }

  @Override
  protected double generateGreedySolutionValue(ProblemInstance instance,
      IPhenotypeEvaluator<QapPhenotype> evaluator) throws Exception {
    QapProblemInstance pi = (QapProblemInstance) instance;
    Integer[] greedy = AssignmentProvider.createGreedyAssignment(pi.getFacilityLocation());
    return evaluator.evaluate(new QapPhenotype(greedy))[0];
  }

  @Override
  protected Map<String, Double> getOptimalValues() throws Exception {
    return getOptimalValues("/org/seage/problem/qap/solutions/__optimal.txt");
  }
}
