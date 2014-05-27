package org.seage.aal.algorithm.grasp;

import org.seage.aal.algorithm.AlgorithmAdapterImpl;
import org.seage.aal.algorithm.AlgorithmParams;
import org.seage.aal.reporter.AlgorithmReport;

public abstract class GraspAlgorithmAdapter extends AlgorithmAdapterImpl{

	public void setParameters(AlgorithmParams params) throws Exception 
	{		
	}

	@Override
	public void startSearching(AlgorithmParams params) throws Exception {
		if(params==null)
    		throw new Exception("Parameters not set");
    	setParameters(params);
		
	}

	@Override
	public void stopSearching() throws Exception
	{
		
	}

	@Override
	public boolean isRunning() 
	{
		return false;
	}

	@Override
	public AlgorithmReport getReport() throws Exception 
	{
		return null;
	}

}
