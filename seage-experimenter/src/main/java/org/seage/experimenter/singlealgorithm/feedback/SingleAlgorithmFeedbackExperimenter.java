package org.seage.experimenter.singlealgorithm.feedback;

import java.util.zip.ZipOutputStream;

import org.seage.aal.problem.InstanceInfo;
import org.seage.aal.problem.ProblemInfo;
import org.seage.experimenter.Experimenter;

public class SingleAlgorithmFeedbackExperimenter extends Experimenter
{

	public SingleAlgorithmFeedbackExperimenter() throws Exception
	{
		super("SingleAlgorithmFeedback");
	}

	@Override
	protected void runExperimentTasks(long experimentID, ProblemInfo problemInfo, InstanceInfo instanceInfo, String[] algorithmIDs, int numConfigs, long timeoutS, ZipOutputStream zos)
			throws Exception
	{
		try
		{
			new FeedbackConfigurator();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
