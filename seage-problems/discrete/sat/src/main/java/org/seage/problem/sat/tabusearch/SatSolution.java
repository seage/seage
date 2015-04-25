package sat.algorithm.tabusearch;

import sat.data.*;
import ailibrary.algorithm.tabusearch.*;
/**
 * Summary description for SatSolution.
 */
public class SatSolution extends SolutionAdapter
{

	private boolean[] _literalValues;
	private int _hash; 

	public SatSolution(int numLiteral)
	{
		_literalValues = new boolean[numLiteral];
		_hash = GeneralSatEvaluator.hashCode(_literalValues);
	}

	public boolean[] getLiteralValues()
	{
		return _literalValues;
	}

	public Object clone()
	{
		SatSolution copy = (SatSolution)super.clone();

		copy._literalValues = new boolean[_literalValues.length];
		for (int i = 0; i < _literalValues.length; i++)
		{
			copy._literalValues[i] = _literalValues[i];
		}
		
		return copy;
	}

	public String toString()
	{
		String result = super.toString();

		String str = "";
		for (int i = 0; i < _literalValues.length; i++)
		{
			int val = _literalValues[i]==true?1:0;
			str += val;
		}
		return result + "\t" + str;
	}
	
	public int hashCode()
	{
		return _hash;
	}
}
