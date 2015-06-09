package org.seage.problem.sat.genetics;
import org.seage.metaheuristic.genetics.BasicGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;

/**
 * Summary description for SatGeneticOperator.
 */
public class SatGeneticOperator extends BasicGeneticOperator<Subject<Boolean>, Boolean>
{
	public Subject<Boolean> mutate(Subject<Boolean> subject) throws Exception
	{
		try
		{
			int length = subject.getChromosome().getLength();
			int count = _random.nextInt((int)(length * _mutateLengthCoef) + 1);
			Subject<Boolean> mutant = (Subject<Boolean>)subject.clone();

			for (int i = 0; i < count; i++)
			{
				int ix = _random.nextInt(length);
				boolean x = mutant.getChromosome().getGene(ix);
				mutant.getChromosome().setGene(ix, !x);
			}
			return mutant;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	public Subject<Boolean> randomize(Subject<Boolean> subject) throws Exception
	{
		try
		{
			int length = subject.getChromosome().getLength();

			Subject<Boolean> random = (Subject<Boolean>)subject.clone();

			for (int i = 0; i < length * 10; i++)
			{
				int ix = _random.nextInt(length - 2) + 1;
				boolean x = random.getChromosome().getGene(ix);
				random.getChromosome().setGene(ix, !x);
				random.getChromosome().setGene(ix + 1, !x);
				random.getChromosome().setGene(ix - 1, !x);
				random.getChromosome().setGene(length - ix - 1, !x);
				random.getChromosome().setGene(length - ix, !x);
				random.getChromosome().setGene(length - ix - 2, !x);
			}
			return random;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}
}
