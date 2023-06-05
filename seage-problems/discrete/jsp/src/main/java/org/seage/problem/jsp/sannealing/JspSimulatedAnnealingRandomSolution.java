package org.seage.problem.jsp.sannealing;

import java.util.Random;
import org.seage.problem.jsp.JspJobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.JspScheduleProvider;

/**
 * .
 * This class represents the solutions on which
 * was applied the random algorithm
 *
 * @author David Omrai
 */
public class JspSimulatedAnnealingRandomSolution extends JspSimulatedAnnealingSolution {
  public JspSimulatedAnnealingRandomSolution(
      JspPhenotypeEvaluator jspPhenoEval, JspJobsDefinition jobs) throws Exception {
    super(JspScheduleProvider.createRandomSchedule(
        jspPhenoEval, jobs, new Random().nextLong()).getSolution());
  }
}
