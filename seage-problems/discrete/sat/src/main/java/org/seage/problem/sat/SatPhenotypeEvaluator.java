package org.seage.problem.sat;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemScoreCalculator;

public class SatPhenotypeEvaluator implements IPhenotypeEvaluator<SatPhenotype> {
  private Formula formula;
  private ProblemScoreCalculator scoreCalculator;

  public SatPhenotypeEvaluator(Formula formula, ProblemInfo problemInfo) {
    this.formula = formula;
    this.scoreCalculator = new ProblemScoreCalculator(problemInfo);
  }

  @Override
  public int compare(double[] arg0, double[] arg1) {
    return (int) arg0[0] - (int) arg1[0];
  }

  @Override
  public double[] evaluate(SatPhenotype phenotypeSubject) throws Exception {
    String instanceID = formula.getProblemInstanceInfo().getInstanceID();
    double objValue = FormulaEvaluator.evaluate(formula, phenotypeSubject.getSolution());
    return new double[] { 
        objValue,
        this.scoreCalculator.calculateInstanceScore(instanceID, objValue)
    };
  }

}
