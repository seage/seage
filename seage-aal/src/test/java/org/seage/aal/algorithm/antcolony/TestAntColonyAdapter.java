package org.seage.aal.algorithm.antcolony;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
		{
			ArrayList<Integer> nodes =  new ArrayList<Integer>();
			for(int j=0;j<source[i].length;j++)
				nodes.add((Integer)source[i][j]);
			_ants[i] = new Ant(nodes);
		}
	}

	@Override
	public Object[][] solutionsToPhenotype() throws Exception
	{
		Arrays.sort(_ants, new Comparator<Ant>()
		{
			@Override
			public int compare(Ant o1, Ant o2)
			{
				// TODO Auto-generated method stub
				return (int)(o1.getDistanceTravelled()-o2.getDistanceTravelled());
			}
			
		});
		Object[][] result = new Object[_ants.length][];
		for(int i=0;i<_ants.length;i++)
			result[i] = _ants[i].getNodeIDs().toArray(new Integer[]{});
		return result;
	}

}
