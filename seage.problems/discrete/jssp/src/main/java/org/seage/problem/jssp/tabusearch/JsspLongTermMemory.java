package jssp.algorithm.tabusearch;

import ailibrary.algorithm.tabusearch.*;
/**
 * Summary description for JsspLongTermMemory.
 */
public class JsspLongTermMemory implements LongTermMemory
{ 
	public JsspLongTermMemory()
	{
		//
		// TODO: Add Constructor Logic here
		//
	}

	public void clearMemory()
	{
	}
	
	public void memorizeSolution(Solution soln, boolean newBestSoln)
	{
		//if (newBestSoln)
		//    System.out.println(soln);
	}
	
	public Solution diversifySolution()
	{
		return null;
	}

	public void resetIterNumber()
	{ 
	}
}
