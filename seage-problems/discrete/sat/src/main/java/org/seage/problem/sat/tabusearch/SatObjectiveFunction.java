package sat.algorithm.tabusearch;

import sat.data.*;
import ailibrary.algorithm.tabusearch.*;
import ailibrary.data.*;

/**
 * Summary description for KnapObjectiveFunction.
 */
public class SatObjectiveFunction implements ObjectiveFunction
{
	private GeneralSatEvaluator _evaluator;

	public SatObjectiveFunction(GeneralSatEvaluator evaluator)
	{
		_evaluator = evaluator;
	}

	public double[] evaluate( Solution solution, Move move) throws Exception
    {
		try
		{
			SatSolution satSol = (SatSolution)solution;
			SatMove satMove = (SatMove)move;
			boolean[] literalValues = (boolean[])satSol.getLiteralValues().clone();

			if (satMove != null)
			{
				literalValues[satMove.getIndexes()[0]] = satMove.getValues()[0] == 1 ? true : false;
				literalValues[satMove.getIndexes()[1]] = satMove.getValues()[1] == 1 ? true : false;
			}

			return _evaluator.evaluate(literalValues);
		}
		catch (Exception ex)
		{
			throw ex;
		}
    }

}
