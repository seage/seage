package org.seage.problem.sat.grasp;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.grasp.GraspAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;

@Annotations.AlgorithmId("GRASP")
@Annotations.AlgorithmName("GRASP")
public class SatGraspAlgorithmFactory implements IAlgorithmFactory
{

	@Override
	public Class<?> getAlgorithmClass()
	{
		return GraspAlgorithmAdapter.class;
	}

	@Override
	public IAlgorithmAdapter createAlgorithm(ProblemInstance instance)
	        throws Exception
	{		
		return new GraspAlgorithmAdapter() {
			
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
