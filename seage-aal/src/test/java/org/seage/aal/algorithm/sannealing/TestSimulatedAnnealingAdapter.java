package org.seage.aal.algorithm.sannealing;

import org.junit.Assert;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

public class TestSimulatedAnnealingAdapter extends SimulatedAnnealingAdapter
{
    private TestSolution[] _solutions0;

    public TestSimulatedAnnealingAdapter(Solution initialSolution, IObjectiveFunction objectiveFunction,
            IMoveManager moveManager, boolean maximizing, String searchID) throws Exception
    {
        super(objectiveFunction, moveManager, maximizing, searchID);
    }

    @Override
    public void solutionsFromPhenotype(Object[][] source) throws Exception
    {
        _solutions0 = new TestSolution[source.length];
        _solutions = new TestSolution[source.length];

        for (int i = 0; i < source.length; i++)
        {
            TestSolution s = new TestSolution(source[i]);
            _solutions0[i] = s;
            _solutions[i] = s;
        }
    }

    @Override
    public Object[][] solutionsToPhenotype() throws Exception
    {
        Assert.assertEquals(_solutions0.length, _solutions.length);
        Assert.assertNotSame(_solutions0[0], _solutions[0]);
        for (int i = 1; i < _solutions.length; i++)
        {
            Assert.assertSame(_solutions0[i], _solutions[i]);
        }
        return null;
    }

}
