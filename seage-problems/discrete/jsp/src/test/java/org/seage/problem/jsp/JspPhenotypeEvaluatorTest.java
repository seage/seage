package org.seage.problem.jsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
public class JspPhenotypeEvaluatorTest
{
  private JobsDefinition _jobsDefinition;
      
  public JspPhenotypeEvaluatorTest() throws Exception
  {
    _jobsDefinition = new JobsDefinition(
        new ProblemInstanceInfo("yn_3x3_example", ProblemInstanceOrigin.RESOURCE, ""), 
        getClass().getResourceAsStream("/org/seage/problem/jsp/test-instances/yn_3x3_example.xml"));
  }

  @Test
  public void testEvaluateSchedule() throws Exception
  {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator evaluator = new JspPhenotypeEvaluator(pi, _jobsDefinition);
    
    Integer[] jobArray = new Integer[] {1,2,3,3,1,2,2,1,3};
    double[] objVal =  evaluator.evaluateSchedule(jobArray);
    assertEquals (12, (int)objVal[0]);
    
    jobArray = new Integer[] {1,2,3,1,2,3,1,2,3};
    objVal =  evaluator.evaluateSchedule(jobArray);
    assertEquals (12, (int)objVal[0]);
    Schedule schedule = evaluator.createSchedule(jobArray);
    assertNotNull(schedule);
  }

}
