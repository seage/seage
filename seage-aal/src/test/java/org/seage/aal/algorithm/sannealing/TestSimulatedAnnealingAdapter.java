package org.seage.aal.algorithm.sannealing;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

public class TestSimulatedAnnealingAdapter extends SimulatedAnnealingAdapter
{

    public TestSimulatedAnnealingAdapter(Solution initialSolution, IObjectiveFunction objectiveFunction, IMoveManager moveManager, boolean maximizing, String searchID) throws Exception
    {
        super(initialSolution, objectiveFunction, moveManager, maximizing, searchID);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void solutionsFromPhenotype(Object[][] source) throws Exception
    {
        _initialSolution = new TestSolution(source[0]);
        
    }

    @Override
    public Object[][] solutionsToPhenotype() throws Exception
    {
        List<Object[]> result = new ArrayList<Object[]>();
        
       
        result.add(((TestSolution)_initialSolution).solution);
        
        return (Object[][]) result.toArray(new Object[][]{});
    }

}
