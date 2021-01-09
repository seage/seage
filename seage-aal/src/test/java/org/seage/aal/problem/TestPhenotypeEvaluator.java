package org.seage.aal.problem;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;

public class TestPhenotypeEvaluator implements IPhenotypeEvaluator<TestPhenotype> {
  @Override
  public double[] evaluate(TestPhenotype phenotypeSubject) throws Exception {
    return new double[] {0.0};
  }

  @Override
  public int compare(double[] arg0, double[] arg1) {
    return (int)(arg0[0] - arg1[0]);
  }

}