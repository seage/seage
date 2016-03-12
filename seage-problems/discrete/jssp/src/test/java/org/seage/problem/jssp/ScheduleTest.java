package org.seage.problem.jssp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
import org.seage.data.Pair;

public class ScheduleTest
{
    private JobsDefinition _jobsDefinition;
    
    public ScheduleTest() throws Exception
    {
        _jobsDefinition = new JobsDefinition(
                new ProblemInstanceInfo("TestJsspInstance", ProblemInstanceOrigin.RESOURCE, ""), 
                getClass().getResourceAsStream("/org/seage/problem/jssp/test-instances/test01.xml"));
    }
    
    @Test
    public void testCreateSchedule() throws Exception
    {
        JsspPhenotypeEvaluator evaluator = new JsspPhenotypeEvaluator(_jobsDefinition);
        Integer[] jobArray = new Integer[] {1,1,1,2,2,2};
        double[] objVal =  evaluator.evaluateSchedule(jobArray, true);
        assertEquals (12, (int)objVal[0]);
        Schedule s = evaluator.getSchedule();
        assertNotNull(s);
        List<Pair<ScheduleCell>> criticalPath = s.findCriticalPath(); 
        assertNotNull(criticalPath);
        assertEquals(1, criticalPath.size());
    }

}
