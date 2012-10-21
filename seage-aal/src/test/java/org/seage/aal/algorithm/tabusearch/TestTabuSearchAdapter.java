package org.seage.aal.algorithm.tabusearch;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.tabusearch.LongTermMemory;
import org.seage.metaheuristic.tabusearch.MoveManager;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;

public class TestTabuSearchAdapter extends TabuSearchAdapter
{
    
    public TestTabuSearchAdapter(MoveManager moveManager, ObjectiveFunction objectiveFunction, String searchID)
    {
        super(moveManager, objectiveFunction, searchID);        
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
