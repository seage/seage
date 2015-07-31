package org.seage.problem.sat.antcolony;

import java.util.ArrayList;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.antcolony.AntColonyAdapter;
import org.seage.aal.problem.ProblemInstance;
import org.seage.metaheuristic.antcolony.Ant;
import org.seage.metaheuristic.antcolony.AntColony;
import org.seage.metaheuristic.antcolony.Graph;
import org.seage.problem.sat.Formula;
import org.seage.problem.sat.FormulaEvaluator;

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
		Graph graph = new SatGraph(formula, new FormulaEvaluator(formula));
        SatAntBrain brain = new SatAntBrain(graph, formula);       

        return new AntColonyAdapter(brain, graph) {
			
			@Override
			public void solutionsFromPhenotype(Object[][] source) throws Exception 
			{
				_ants = new Ant[source.length];
				for(int i=0;i<_ants.length;i++)
				{
					ArrayList<Integer> nodes = new ArrayList<Integer>();
					for(int j=1;j<=source[i].length;j++)
						nodes.add((Boolean)source[i][j-1]==true?j:-j);
					_ants[i] = new Ant(nodes);
				}	
			}
			@Override
			public Object[][] solutionsToPhenotype() throws Exception 
			{
				Object[][] result = new Object[_ants.length][];
				for(int i=0;i<_ants.length;i++)
				{
					result[i] = new Boolean[_ants[i].getNodeIDsAlongPath().size()];
					for(int j=0;j<result[i].length;j++)
					{						
						result[i][j] = _ants[i].getNodeIDsAlongPath().get(j)>0;//.toArray(new Integer[]{});
					}	
				}
				return result;
			}
			
		};
	}

}
