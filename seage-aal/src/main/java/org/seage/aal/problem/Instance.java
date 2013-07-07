package org.seage.aal.problem;

public abstract class Instance
{
	protected InstanceInfo _instanceInfo;

	public Instance(InstanceInfo instanceInfo)
	{
		super();
		_instanceInfo = instanceInfo;
	}

	public InstanceInfo getInstanceInfo()
	{
		return _instanceInfo;
	}

}
