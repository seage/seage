package org.seage.problem.jsp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.data.Pair;

public class ScheduleTest {
  private JobsDefinition _jobsDefinition;

  public ScheduleTest() throws Exception {
    _jobsDefinition = new JobsDefinition(
        new ProblemInstanceInfo("yn_3x3_example", ProblemInstanceOrigin.RESOURCE, ""),
        getClass().getResourceAsStream("/org/seage/problem/jsp/test-instances/yn_3x3_example.xml"));
  }
  
  @Test
  public void testInitSchedule() throws Exception {
    Integer[] jobArray = new Integer[] {1, 2, 3, 3, 1, 2, 2, 1, 3};
    Schedule s = new Schedule(_jobsDefinition);
    s.createSchedule(jobArray);
    assertEquals(12, s.getMakeSpan());
    var criticalPath = s.findCriticalPath();
    assertNotNull(criticalPath);
    assertEquals(2, criticalPath.size());
  }

  @Test
  public void testCreateSchedule() throws Exception {
    Schedule s = new Schedule(_jobsDefinition);
    s.createSchedule(new Integer[] {1});
  }

  @Test
  public void testEvaluateSchedule() throws Exception {
    JspProblemProvider problemProvider = new JspProblemProvider();
    ProblemInfo pi = problemProvider.getProblemInfo();
    JspPhenotypeEvaluator evaluator = new JspPhenotypeEvaluator(pi, _jobsDefinition);
    Integer[] jobArray = new Integer[] {1, 2, 3, 3, 1, 2, 2, 1, 3};
    double[] objVal = evaluator.evaluateSchedule(jobArray);
    assertEquals(12, (int) objVal[0]);
    Schedule s = new Schedule(_jobsDefinition, jobArray);
    assertNotNull(s);
    List<Pair<ScheduleCell>> criticalPath = s.findCriticalPath();
    assertNotNull(criticalPath);
    assertEquals(2, criticalPath.size());
  }

}
