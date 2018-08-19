package org.seage.aal.algorithm.antcolony;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.seage.aal.algorithm.IPhenotypeEvaluator;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;

public class TestAntColonyAdapter extends AntColonyAdapter<TestPhenotype, Ant>
{
    private Ant[] _ants0;

    public TestAntColonyAdapter(AntBrain brain, Graph graph, IPhenotypeEvaluator<TestPhenotype> phenotypeEvaluator)
    {
        super(brain, graph, phenotypeEvaluator);
    }

    @Override
    public void solutionsFromPhenotype(TestPhenotype[] source) throws Exception
    {
        _ants0 = new Ant[source.length];
        _ants = new Ant[source.length];

        for (int i = 0; i < _ants.length; i++)
        {
            ArrayList<Integer> nodes = new ArrayList<Integer>();
            nodes.addAll(Arrays.asList(source[i].getSolution()));            

            _ants0[i] = new Ant(nodes);
            _ants[i] = new Ant(nodes);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public TestPhenotype[] solutionsToPhenotype() throws Exception
    {
        assertEquals(_ants0.length, _ants.length);

        boolean notSame = false;
        for (int i = 0; i < _ants.length; i++)
        {
            assertEquals(_ants0[i].getNodeIDsAlongPath().size(), _ants[i].getNodeIDsAlongPath().size());

            for (int j = 0; j < _ants0[i].getNodeIDsAlongPath().size(); j++)
            {
                if (_ants0[i].getNodeIDsAlongPath().get(j) != _ants[i].getNodeIDsAlongPath().get(j))
                    ;
                {
                    notSame = true;
                    break;
                }
            }
        }
        assertTrue(notSame);
        return null;
    }

	@Override
	public TestPhenotype solutionToPhenotype(Ant solution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
