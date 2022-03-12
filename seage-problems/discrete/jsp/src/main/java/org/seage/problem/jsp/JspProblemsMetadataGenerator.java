package org.seage.problem.jsp;

import java.util.Map;
import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInstance;
import org.seage.aal.problem.ProblemMetadataGenerator;

public class JspProblemsMetadataGenerator extends ProblemMetadataGenerator<JspPhenotype>{

  public JspProblemsMetadataGenerator(JspProblemProvider jspProblemProvider) {}

  @Override
  protected double generateRandomResult(ProblemInstance instance,
      IPhenotypeEvaluator<JspPhenotype> evaluator) throws Exception {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected double generateGreedyResult(ProblemInstance instance,
      IPhenotypeEvaluator<JspPhenotype> evaluator) throws Exception {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  protected Map<String, Double> getOptimalValues() {
    // TODO Auto-generated method stub
    return null;
  }

}
