package org.seage.aal.algorithm.antcolony;

import java.util.ArrayList;

import org.seage.aal.algorithm.Phenotype;
import org.seage.aal.algorithm.TestPhenotype;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;

import junit.framework.Assert;

public class TestAntColonyAdapter extends AntColonyAdapter<Ant>
{
    private Ant[] _ants0;

    public TestAntColonyAdapter(AntBrain brain, Graph graph)
    {
        super(brain, graph);
    }

    @Override
    public void solutionsFromPhenotype(Phenotype<>[] source) throws Exception
    {
        _ants0 = new Ant[source.length];
        _ants = new Ant[source.length];

        for (int i = 0; i < _ants.length; i++)
        {
            ArrayList<Integer> nodes = new ArrayList<Integer>();
            for (int j = 0; j < source[i].length; j++)
                nodes.add((Integer) source[i][j]);

            _ants0[i] = new Ant(nodes);
            _ants[i] = new Ant(nodes);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public Phenotype<?>[] solutionsToPhenotype() throws Exception
    {
        Assert.assertEquals(_ants0.length, _ants.length);

        boolean notSame = false;
        for (int i = 0; i < _ants.length; i++)
        {
            Assert.assertEquals(_ants0[i].getNodeIDsAlongPath().size(), _ants[i].getNodeIDsAlongPath().size());

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
        Assert.assertTrue(notSame);
        return null;
    }

	@Override
	public Phenotype<?> solutionToPhenotype(Ant solution) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
