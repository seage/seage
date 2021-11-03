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
  }

  @Test
  public void testCriticalPath() throws Exception {
    // Initital
    Integer[] jobArray = new Integer[] {1, 1, 1, 2, 2, 2, 3, 3, 3};
    Schedule s1 = new Schedule(_jobsDefinition, jobArray);
    assertNotNull(s1);
    assertEquals(22, s1.getMakeSpan());
    List<Pair<ScheduleCell>> criticalPath1 = s1.findCriticalPath();
    assertNotNull(criticalPath1);
    assertEquals(2, criticalPath1.size());
    assertEquals(5, criticalPath1.get(0).getFirst().getIndex());
    assertEquals(6, criticalPath1.get(0).getSecond().getIndex());
    assertEquals(2, criticalPath1.get(1).getFirst().getIndex());
    assertEquals(4, criticalPath1.get(1).getSecond().getIndex());

    // Optimal
    Integer[] jobArray2 = new Integer[] {1, 2, 3, 3, 1, 2, 2, 1, 3};
    Schedule s2 = new Schedule(_jobsDefinition, jobArray2);
    assertNotNull(s2);
    assertEquals(12, s2.getMakeSpan());
    List<Pair<ScheduleCell>> criticalPath2 = s2.findCriticalPath();
    assertNotNull(criticalPath2);
    assertEquals(2, criticalPath2.size());
    assertEquals(0, criticalPath2.get(0).getFirst().getIndex());
    assertEquals(1, criticalPath2.get(0).getSecond().getIndex());
    assertEquals(5, criticalPath2.get(1).getFirst().getIndex());
    assertEquals(7, criticalPath2.get(1).getSecond().getIndex());

    // Optimal 2
    Integer[] jobArray3 = new Integer[] {2, 1, 3, 2, 1, 3, 2, 1, 3};
    Schedule s3 = new Schedule(_jobsDefinition, jobArray3);
    assertNotNull(s3);
    assertEquals(12, s3.getMakeSpan());
    List<Pair<ScheduleCell>> criticalPath3 = s3.findCriticalPath();
    assertNotNull(criticalPath3);
    assertEquals(2, criticalPath3.size());
    assertEquals(0, criticalPath3.get(0).getFirst().getIndex());
    assertEquals(1, criticalPath3.get(0).getSecond().getIndex());
    assertEquals(4, criticalPath3.get(1).getFirst().getIndex());
    assertEquals(6, criticalPath3.get(1).getSecond().getIndex());
  }

}
