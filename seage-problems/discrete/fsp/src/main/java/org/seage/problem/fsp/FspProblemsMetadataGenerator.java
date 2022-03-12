package org.seage.problem.fsp;

import java.util.Map;
import org.seage.problem.jsp.JspProblemsMetadataGenerator;

public class FspProblemsMetadataGenerator extends JspProblemsMetadataGenerator {

  public FspProblemsMetadataGenerator(FspProblemProvider fspProblemProvider) {
    super(fspProblemProvider);
  }

  @Override
  protected Map<String, Double> getOptimalValues() throws Exception {
    return getOptimalValues("/org/seage/problem/fsp/solutions/__optimal.txt");
  }
}
