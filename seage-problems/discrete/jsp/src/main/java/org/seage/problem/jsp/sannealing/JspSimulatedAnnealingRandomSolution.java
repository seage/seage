package org.seage.problem.jsp.sannealing;

import java.util.Random;
import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.ScheduleProvider;

/**
 * This class represents the solutions on which
 * was applied the random algorithm
 * 
 * @author David Omrai
 */
public class JspSimulatedAnnealingRandomSolution extends JspSimulatedAnnealingSolution {
  public JspSimulatedAnnealingRandomSolution(JspPhenotypeEvaluator jspPhenoEval, JobsDefinition jobs) throws Exception {
    super(ScheduleProvider.createRandomSchedule(jspPhenoEval, jobs, new Random().nextLong()).getSolution());
  }
}
