package org.seage.aal.algorithm.fireflies;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;


public class TestFireflyAlgorithmAdapter extends FireflyAlgorithmAdapter
{
    
    public TestFireflyAlgorithmAdapter(FireflyOperator operator,
            ObjectiveFunction evaluator,
            boolean maximizing,
            String  searchID)
    {
        super(operator, evaluator, maximizing, "");     
    }

    @Override
    public void solutionsFromPhenotype(Object[][] source) throws Exception
    {
        _solutions = new TestSolution[source.length];
        
        for(int i=0;i<source.length;i++)
        {
            _solutions[i] = new TestSolution(source[i]);
        }
        
    }

    @Override
    public Object[][] solutionsToPhenotype() throws Exception
    {
        List<Object[]> result = new ArrayList<Object[]>();
        
        for(int i=0;i<_solutions.length;i++)
        {
            result.add(((TestSolution)_solutions[i]).solution);
        }
        return (Object[][]) result.toArray(new Object[][]{});
    }

}
