package org.seage.problem.jssp;

/**
 * Summary description for Class1.
 */
public class JobInfo
{
	
	private int _id;
	private int _priority;

	public JobInfo(int id, int priority) 
	{
		_id = id;
		_priority = priority;

	}

	public int getID()
	{
		return _id;
	}

	public int getPriority()
	{		
		return _priority;
	}
	
	
}
