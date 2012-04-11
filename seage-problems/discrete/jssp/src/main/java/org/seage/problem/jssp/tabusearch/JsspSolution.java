package jssp.algorithm.tabusearch;

import ailibrary.algorithm.tabusearch.*;
/**
 * Summary description for JsspSolution.
 */
public class JsspSolution extends SolutionAdapter
{
	private int[] _jobArray;
 
	public JsspSolution(int[] jobArray)
	{
		_jobArray = (int[])jobArray.clone();
	}

	public int[] getJobArray()
	{
		return _jobArray;
	}

	public String toString()
	{
		return getObjectiveValue()[0] + "";
	}

	public Object clone()
	{
		JsspSolution copy = (JsspSolution)super.clone();
		copy._jobArray = (int[])this._jobArray.clone();
		return copy;
	}   // end clone
}
