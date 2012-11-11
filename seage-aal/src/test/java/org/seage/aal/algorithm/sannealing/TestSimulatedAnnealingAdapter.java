package org.seage.aal.algorithm.sannealing;

import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

public class TestSimulatedAnnealingAdapter extends SimulatedAnnealingAdapter
{

    public TestSimulatedAnnealingAdapter(Solution initialSolution, IObjectiveFunction objectiveFunction, IMoveManager moveManager, boolean maximizing, String searchID) throws Exception
    {
        super(objectiveFunction, moveManager, maximizing, searchID);
    }
    
    public void solutionsFromPhenotype(Object[][] source) throws Exception 
    {
    	_solutions = new Solution[source.length];
    	for(int j=0;j<source.length;j++)
    	{
    		TestSolution solution = new TestSolution(source[j]);
                    
            _solutions[j] = solution;
    	}
    }

    public Object[][] solutionsToPhenotype() throws Exception
    {
        Object[][] source = new Object[_solutions.length][ ];

        for(int j=0;j<source.length;j++)
    	{
        	source[j] = ((TestSolution)_solutions[j]).solution;
    	}
        return source;
    }

}
