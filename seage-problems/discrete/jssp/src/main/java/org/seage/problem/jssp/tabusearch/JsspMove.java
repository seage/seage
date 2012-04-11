package jssp.algorithm.tabusearch;

import ailibrary.algorithm.tabusearch.*;
/**
 * Summary description for JsspMove.
 */
public class JsspMove implements Move
{
	private int _ix1;
	private int _ix2;

	public JsspMove(int ix1, int ix2)
	{
		_ix1 = ix1;
		_ix2 = ix2;
	}

	public void operateOn(Solution soln)
	{
		JsspSolution solution = (JsspSolution)soln;
		int tmp = solution.getJobArray()[_ix1];
		solution.getJobArray()[_ix1] = solution.getJobArray()[_ix2];
		solution.getJobArray()[_ix2] = tmp;
	}

	public int getIndex1()
	{
		return _ix1;
	}

	public int getIndex2()
	{
		return _ix2;
	}

	public int hashCode()
	{
		return (_ix1 << 10) + _ix2 << 5;
	}
}
