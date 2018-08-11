package org.seage.experimenter.multialgorithm;

import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.experimenter.Experimenter;

public class MultiAlgorithmRandomExperimenter extends Experimenter 
{

	public MultiAlgorithmRandomExperimenter(String experimentName) 
	{
		super(experimentName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void performExperiment(String experimentID, ProblemInfo problemInfo, ProblemInstanceInfo instanceInfo,
			String[] algorithmIDs) throws Exception 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected long getEstimatedTime(int instancesCount, int algorithmsCount) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long getNumberOfConfigs(int instancesCount, int algorithmsCount) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
