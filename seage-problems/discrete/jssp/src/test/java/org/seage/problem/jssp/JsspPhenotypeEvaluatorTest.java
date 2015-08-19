package org.seage.problem.jssp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.aal.problem.ProblemInstanceInfo.ProblemInstanceOrigin;

public class JsspPhenotypeEvaluatorTest
{
    
    @Test
    public void test() throws Exception
    {
        JobsDefinition jobsDefinition = new JobsDefinition(
                new ProblemInstanceInfo("TestJsspInstance", ProblemInstanceOrigin.RESOURCE, ""), 
                getClass().getResourceAsStream("/org/seage/problem/jssp/test01.xml"));
        
        JsspPhenotypeEvaluator evaluator = new JsspPhenotypeEvaluator(jobsDefinition);
        
        Integer[] schedule = new Integer[] {1,1,1,2,2,2};
        double[] objVal =  evaluator.evaluate(schedule);
        assertEquals (12, (int)objVal[0]);
        
        schedule = new Integer[] {1,2,1,2,1,2};
        objVal =  evaluator.evaluate(schedule);
        assertEquals (6, (int)objVal[0]);
    }

}
