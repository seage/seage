package org.seage.aal.algorithm.sannealing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.sannealing.IMoveManager;
import org.seage.metaheuristic.sannealing.IObjectiveFunction;
import org.seage.metaheuristic.sannealing.Solution;

public class TestSimulatedAnnealingAdapter extends SimulatedAnnealingAdapter<TestPhenotype, TestSolution>
{
    private TestSolution[] _solutions0;

    public TestSimulatedAnnealingAdapter(Solution initialSolution, IObjectiveFunction objectiveFunction,
            IMoveManager moveManager, IPhenotypeEvaluator<TestPhenotype> phenotypeEvaluator, boolean maximizing) throws Exception
    {
        super(objectiveFunction, moveManager, phenotypeEvaluator, maximizing);
    }

    @Override
    public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception
    {
        _solutions0 = new TestSolution[source.length];
        _solutions = new TestSolution[source.length];

        for (int i = 0; i < source.length; i++)
        {
            TestSolution s = new TestSolution(source[i].getSolution());
            _solutions0[i] = s;
            _solutions[i] = s;
        }
    }

    @Override
    public TestPhenotype[] solutionsToPhenotype() throws Exception
    {
        assertEquals(_solutions0.length, _solutions.length);
        assertNotSame(_solutions0[0], _solutions[0]);
        for (int i = 1; i < _solutions.length; i++)
        {
            assertSame(_solutions0[i], _solutions[i]);
        }
        return null;
    }

	@Override
	public TestPhenotype solutionToPhenotype(TestSolution solution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
