package org.seage.problem.sat.antcolony;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Formula;

@Annotations.AlgorithmId("AntColony")
@Annotations.AlgorithmName("AntColony")
public class SatAntColonyFactory implements IAlgorithmFactory
{

	@Override
	public Class<?> getAlgorithmClass()
	{
		return AntColonyAdapter.class;
	}

	@Override
	public IAlgorithmAdapter createAlgorithm(ProblemInstance instance)
	        throws Exception
	{
		Formula formula = (Formula)instance;
		Graph graph = new SatGraph2(formula);
        SatAntBrain brain = new SatAntBrain(graph, formula);       

        return new AntColonyAdapter(brain, graph) {
			
			@Override
			public Object[][] solutionsToPhenotype() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void solutionsFromPhenotype(Object[][] source) throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
	}

}
