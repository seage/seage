package sat.algorithm.genetics;

import sat.data.*;
import ailibrary.algorithm.genetics.*;
import ailibrary.data.DataStore;
/**
 * Summary description for SatObjectiveFunction.
 */
public class SatEvaluator implements Evaluator
{
	private GeneralSatEvaluator _evaluator;
	public SatEvaluator(GeneralSatEvaluator evaluator)
	{
		_evaluator = evaluator;
	}

	public double[] evaluate(Subject solution)
	{
		Chromosome chrom = solution.getGenome().getChromosome(0);
		boolean[] array = new boolean[chrom.getLength()];
		for (int i = 0; i < array.length; i++)
		{
			array[i] = chrom.getGene(i).getValue() == 1 ? true : false;
		}
		double[] val = _evaluator.evaluate(array);
		return new double[] { val[0], val[1], val[2] };
	}
}
