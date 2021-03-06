package org.seage.problem.sat;

import org.seage.aal.algorithm.IPhenotypeEvaluator;

public class SatPhenotypeEvaluator implements IPhenotypeEvaluator<SatPhenotype> {
  private Formula _formula;

  public SatPhenotypeEvaluator(Formula formula) {
    _formula = formula;
  }

  @Override
  public int compare(double[] arg0, double[] arg1) {
    return (int) arg0[0] - (int) arg1[0];
  }

  @Override
  public double[] evaluate(SatPhenotype phenotypeSubject) throws Exception {
    return new double[] { FormulaEvaluator.evaluate(_formula, phenotypeSubject.getSolution()) };
  }

}
