package org.seage.aal.algorithm.fireflies;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.fireflies.FireflyOperator;
import org.seage.metaheuristic.fireflies.ObjectiveFunction;

public class TestFireflyAlgorithmAdapter extends FireflyAlgorithmAdapter<TestPhenotype, TestSolution>
{
    private List<TestSolution> _solutions0;

    public TestFireflyAlgorithmAdapter(FireflyOperator operator,
            ObjectiveFunction evaluator,
            IPhenotypeEvaluator<TestPhenotype> phenotypeEvaluator,
            boolean maximizing)
    {
        super(operator, evaluator, phenotypeEvaluator, maximizing);
    }

    @Override
    public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception
    {
        _solutions0 = new ArrayList<TestSolution>(source.length);
        _solutions = new ArrayList<TestSolution>(source.length);

        for (int i = 0; i < source.length; i++)
        {
            TestSolution s = new TestSolution(source[i].getSolution());
            _solutions0.add(s);
            _solutions.add(s);
        }
    }

    @Override
    public TestPhenotype[] solutionsToPhenotype() throws Exception
    {
        assertEquals(_solutions0.size(), _solutions.size());

        for (int i = 1; i < _solutions.size(); i++)
        {
            assertNotSame(_solutions0.get(i), _solutions.get(i));
        }

        return null;
    }

	@Override
	public TestPhenotype solutionToPhenotype(TestSolution solution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
