package org.seage.problem.jsp;

import java.util.Map;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemMetadataGenerator;

public class JspProblemsMetadataGenerator extends ProblemMetadataGenerator<JspPhenotype> {

  public JspProblemsMetadataGenerator(JspProblemProvider jspProblemProvider) {
    super(jspProblemProvider);
  }

  @Override
  protected double generateRandomSolutionValue(ProblemInstance instance,
      IPhenotypeEvaluator<JspPhenotype> evaluator) throws Exception {
    return ScheduleProvider.createRandomSchedule((JspPhenotypeEvaluator) evaluator,
        (JobsDefinition) instance, System.currentTimeMillis()).getObjValue();
  }

  @Override
  protected double generateGreedySolutionValue(ProblemInstance instance,
      IPhenotypeEvaluator<JspPhenotype> evaluator) throws Exception {
    return ScheduleProvider
        .createGreedySchedule((JspPhenotypeEvaluator) evaluator, (JobsDefinition) instance)
        .getObjValue();
  }

  @Override
  protected Map<String, Double> getOptimalValues() throws Exception {
    return getOptimalValues("/org/seage/problem/jsp/solutions/__optimal.txt");
  }

}
