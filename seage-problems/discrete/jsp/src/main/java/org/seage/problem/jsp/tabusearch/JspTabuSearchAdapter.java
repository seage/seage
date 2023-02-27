package org.seage.problem.jsp.tabusearch;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.tabusearch.TabuSearchAdapter;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.problem.jsp.JspPhenotype;

public class JspTabuSearchAdapter extends TabuSearchAdapter<JspPhenotype, JspTabuSearchSolution>{
  public JspTabuSearchAdapter(MoveManager moveManager, ObjectiveFunction objectiveFunction,
      IPhenotypeEvaluator<JspPhenotype> phenotypeEvaluator) {
    super(moveManager, objectiveFunction, phenotypeEvaluator);
  }

  @Override
  public void solutionsFromPhenotype(JspPhenotype[] source) throws Exception {
    this.solutions = new JspTabuSearchSolution[source.length];
    for (int i = 0; i < source.length; i++)
      this.solutions[i] = new JspTabuSearchSolution(source[i].getSolution());
  }

  @Override
  public JspPhenotype[] solutionsToPhenotype() throws Exception {
    JspPhenotype[] result = new JspPhenotype[this.solutions.length];
    for (int i = 0; i < this.solutions.length; i++) {
      result[i] = solutionToPhenotype(this.solutions[i]);
    }
    return result;
  }

  @Override
  public JspPhenotype solutionToPhenotype(JspTabuSearchSolution solution) throws Exception {
    JspPhenotype result = new JspPhenotype(solution.getJobArray());
    double[] objVals = this.phenotypeEvaluator.evaluate(result);
    result.setObjValue(objVals[0]);
    result.setScore(objVals[1]);
    return result;
  }
}
