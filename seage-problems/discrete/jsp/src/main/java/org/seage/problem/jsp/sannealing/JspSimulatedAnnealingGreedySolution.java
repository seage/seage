package org.seage.problem.jsp.sannealing;

import org.seage.problem.jsp.JobsDefinition;
import org.seage.problem.jsp.JspPhenotypeEvaluator;
import org.seage.problem.jsp.ScheduleProvider;

/**
 * This class represents the solutions on which
 * was applied the greedy algorithm
 * 
 * @author David Omrai
 */
public class JspSimulatedAnnealingGreedySolution extends JspSimulatedAnnealingSolution {
  public JspSimulatedAnnealingGreedySolution(JspPhenotypeEvaluator jspPhenoEval, JobsDefinition jobs) throws Exception {
    super(ScheduleProvider.createGreedySchedule(jspPhenoEval, jobs).getSolution());
  }
}
