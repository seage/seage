package org.seage.problem.sat.tabusearch;

import org.seage.metaheuristic.tabusearch.Move;
import org.seage.metaheuristic.tabusearch.ObjectiveFunction;
import org.seage.metaheuristic.tabusearch.Solution;
import org.seage.problem.sat.SatPhenotypeEvaluator;

/**
 * Summary description for KnapObjectiveFunction.
 */
public class SatObjectiveFunction implements ObjectiveFunction
{
	private SatPhenotypeEvaluator _evaluator;

	public SatObjectiveFunction(SatPhenotypeEvaluator evaluator)
	{
		_evaluator = evaluator;
	}

	public double[] evaluate( Solution solution, Move move) throws Exception
    {
		try
		{
			SatSolution satSol = (SatSolution)solution;
			SatMove satMove = (SatMove)move;
			
			Boolean[] literalValues = new Boolean[satSol.getLiteralValues().length];
			//(boolean[])satSol.getLiteralValues().clone();
			for(int i=0;i<satSol.getLiteralValues().length;i++)   
				literalValues[i] = satSol.getLiteralValues()[i];			

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
