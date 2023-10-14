package org.seage.problem.sat;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemScoreCalculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SatPhenotypeEvaluator implements IPhenotypeEvaluator<SatPhenotype> {
  private static Logger log = LoggerFactory.getLogger(SatPhenotypeEvaluator.class.getName());
  
  private Formula formula;
  private ProblemScoreCalculator scoreCalculator;

  public SatPhenotypeEvaluator(ProblemInfo problemInfo, Formula formula) {
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
    int objValue = FormulaEvaluator.evaluate(formula, phenotypeSubject.getSolution());
    //log.debug("instanceid: {} objval: {}", instanceID, objValue);
    return new double[] { 
        objValue,
        this.scoreCalculator.calculateInstanceScore(instanceID, objValue)
    };
  }

}
