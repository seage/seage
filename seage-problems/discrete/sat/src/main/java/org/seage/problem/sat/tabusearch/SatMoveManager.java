package sat.algorithm.tabusearch;

import sat.data.*;
import ailibrary.algorithm.tabusearch.*;
import java.util.*;
/**
 * Summary description for SatMoveManager.
 */
public class SatMoveManager implements MoveManager
{
	//public DataStore _dataStore;
	private SatMove[] _null;
	private ArrayList _moves;
	private Random _random;
	public SatMoveManager(/*DataStore dataStore*/)
	{
		//_dataStore = dataStore;
		_moves = new ArrayList();
		_random = new Random(97789);
		_null = new SatMove[0];
	}
		
	private int[] getTableValue2(int val0, int val1)
	{		
		if (val0 == 1 && val1 == 0)
			return new int[] { 0,  1};
		if (val0 == 0 && val1 == 1)
			return new int[] { 1, 1 };
		if (val0 == 0 && val1 == 0)
			return new int[] { 1, 0 };
		//if (val0 == 1 && val1 == 1)
		return new int[] { 0, 0 };
	}
	private int[] getTableValue(int val0, int val1)
	{
		if (val0 == 1)
			return new int[] { 0, val1 };
		if (val0 == 0)
			return new int[] { 1, val1 };
		
		//if (val0 == 1 && val1 == 1)
		return new int[] { 0, 0 };
	}

	public Move[] getAllMoves(Solution solution)
	{
		SatSolution satSol = (SatSolution)solution;
		_moves.clear();
		int numMoves = Math.min(satSol.getLiteralValues().length, 100);
		for (int i = 0; i < numMoves; i++)
		{
			int ix0 = _random.nextInt(satSol.getLiteralValues().length - 1);
			int ix1 = _random.nextInt(satSol.getLiteralValues().length - 1);
			//int ix0 = i;
			//int ix1 = (i + 1) % satSol.getLiteralValues().length; 
			
			int val0 = satSol.getLiteralValues()[ix0] == true ? 1 : 0;
			int val1 = satSol.getLiteralValues()[ix1] == true ? 1 : 0;
			int[] diff = getTableValue(val0, val1);

			_moves.add(new SatMove(new int[] { ix0, ix1 }, diff));
		}
		//int ix0 = 0;
		//int ix1 = satSol.getLiteralValues().length - 1;
		//int val0 = satSol.getLiteralValues()[ix0] == true ? 1 : 0;
		//int val1 = satSol.getLiteralValues()[ix1] == true ? 1 : 0;
		//int[] diff = getTableValue(val0, val1);

		//result.add(new SatMove(new int[] { ix0, ix1}, diff));

		return (Move[])_moves.toArray(_null);
	}
}