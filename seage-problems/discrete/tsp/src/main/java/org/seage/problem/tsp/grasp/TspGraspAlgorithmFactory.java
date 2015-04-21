package org.seage.problem.tsp.grasp;

import org.seage.aal.Annotations;
import org.seage.aal.algorithm.IAlgorithmAdapter;
import org.seage.aal.algorithm.IAlgorithmFactory;
import org.seage.aal.algorithm.grasp.GraspAlgorithmAdapter;
import org.seage.aal.problem.ProblemInstance;

@Annotations.AlgorithmId("GRASP")
@Annotations.AlgorithmName("GRASP")
public class TspGraspAlgorithmFactory implements IAlgorithmFactory 
{

	@Override
	public Class<GraspAlgorithmAdapter> getAlgorithmClass() 
	{
		return GraspAlgorithmAdapter.class;
	}

	@Override
	public IAlgorithmAdapter createAlgorithm(ProblemInstance instance)
			throws Exception 
	{
		// TODO Auto-generated method stub
		return new TspGraspAlgorithmAdapter();
	}

}
