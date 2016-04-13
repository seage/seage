package org.seage.experimenter.multialgorithm;

import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.ProblemInfo;
import org.seage.aal.problem.ProblemInstanceInfo;
import org.seage.experimenter.Experimenter;

public class MultiAlgorithmExperimenter extends Experimenter 
{

	public MultiAlgorithmExperimenter(String experimentName) 
	{
		super(experimentName);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void performExperiment(long experimentID, ProblemInfo problemInfo, ProblemInstanceInfo instanceInfo,
			String[] algorithmIDs, ZipOutputStream zos) throws Exception 
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
