package org.seage.aal.algorithm.fireflies;

import org.junit.Assert;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;

public class TestFireflyAlgorithmAdapter extends FireflyAlgorithmAdapter
{
    private TestSolution[] _solutions0;

    public TestFireflyAlgorithmAdapter(FireflyOperator operator,
            ObjectiveFunction evaluator,
            boolean maximizing,
            String searchID)
    {
        super(operator, evaluator, maximizing, "");
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

        for (int i = 1; i < _solutions.length; i++)
        {
            Assert.assertNotSame(_solutions0[i], _solutions[i]);
        }

        return null;
    }

}
