package org.seage.aal.algorithm.antcolony;

import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;

public class TestAntColonyAdapter extends AntColonyAdapter
{

	public TestAntColonyAdapter(AntBrain brain, Graph graph)
	{
		super(brain, graph);
	}

	@Override
	public void solutionsFromPhenotype(Object[][] source) throws Exception
	{
		_ants = new Ant[source.length];
		for(int i=0;i<_ants.length;i++)
			_ants[i] = new Ant(_brain, _graph);
	}

	@Override
	public Object[][] solutionsToPhenotype() throws Exception
	{
		Object[][] result = new Object[_ants.length][];
		for(int i=0;i<_ants.length;i++)
			result[i] = new Integer[0];
		return result;
	}

}
