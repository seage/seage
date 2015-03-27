package org.seage.aal.problem;

public abstract class ProblemInstance
{
	protected ProblemInstanceInfo _instanceInfo;

	public ProblemInstance(ProblemInstanceInfo instanceInfo)
	{
		super();
		_instanceInfo = instanceInfo;
	}

	public ProblemInstanceInfo getInstanceInfo()
	{
		return _instanceInfo;
	}

}
