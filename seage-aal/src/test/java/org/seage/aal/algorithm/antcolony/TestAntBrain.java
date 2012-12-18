package org.seage.aal.algorithm.antcolony;

import org.seage.metaheuristic.antcolony.AntBrain;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.metaheuristic.antcolony.Node;

public class TestAntBrain extends AntBrain
{

	public TestAntBrain(Graph graph)
	{
		super(graph);
	}

	@Override
	public double getNodesDistance(Node n1, Node n2)
	{
		return 1;
	}

}
