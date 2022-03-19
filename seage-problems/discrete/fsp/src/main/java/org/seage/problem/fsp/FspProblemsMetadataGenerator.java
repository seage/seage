package org.seage.problem.fsp;

import java.util.Map;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemMetadataGenerator;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotype;
import org.seage.problem.jsp.JspPhenotypeEvaluator;

public class FspProblemsMetadataGenerator extends ProblemMetadataGenerator<JspPhenotype> {

  public FspProblemsMetadataGenerator(FspProblemProvider fspProblemProvider) {
    super(fspProblemProvider);
  }

  @Override
  protected double generateRandomSolutionValue(ProblemInstance instance,
      IPhenotypeEvaluator<JspPhenotype> evaluator) throws Exception {
    return FspScheduleProvider.createRandomSchedule((JspPhenotypeEvaluator) evaluator,
        (JspJobsDefinition) instance, System.currentTimeMillis()).getObjValue();
  }

  @Override
  protected double generateGreedySolutionValue(ProblemInstance instance,
      IPhenotypeEvaluator<JspPhenotype> evaluator) throws Exception {
    return FspScheduleProvider
        .createGreedySchedule((JspPhenotypeEvaluator) evaluator, (JspJobsDefinition) instance)
        .getObjValue();
  }

  @Override
  protected Map<String, Double> getOptimalValues() throws Exception {
    return getOptimalValues("/org/seage/problem/fsp/solutions/__optimal.txt");
  }
}
