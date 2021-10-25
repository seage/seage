package org.seage.problem.jssp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;
public class JsspPhenotypeEvaluatorTest
{
    private JobsDefinition _jobsDefinition;
    
    public JsspPhenotypeEvaluatorTest() throws Exception
    {
        _jobsDefinition = new JobsDefinition(
                new ProblemInstanceInfo("test01", ProblemInstanceOrigin.RESOURCE, ""), 
                getClass().getResourceAsStream("/org/seage/problem/jssp/test-instances/test01.xml"));
    }

    @Test
    public void testEvaluateSchedule() throws Exception
    {
        JsspProblemProvider problemProvider = new JsspProblemProvider();
        ProblemInfo pi = problemProvider.getProblemInfo();
        JsspPhenotypeEvaluator evaluator = new JsspPhenotypeEvaluator(pi, _jobsDefinition);
        
        Integer[] jobArray = new Integer[] {1,1,1,2,2,2};
        double[] objVal =  evaluator.evaluateSchedule(jobArray);
        assertEquals (12, (int)objVal[0]);
        
        jobArray = new Integer[] {1,2,1,2,1,2};
        objVal =  evaluator.evaluateSchedule(jobArray);
        assertEquals (6, (int)objVal[0]);
        Schedule schedule = evaluator.createSchedule(jobArray);
        assertNotNull(schedule);
    }

}
