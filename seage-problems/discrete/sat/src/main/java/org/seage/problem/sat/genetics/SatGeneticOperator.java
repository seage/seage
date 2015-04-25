package sat.algorithm.genetics;
import ailibrary.algorithm.genetics.*;

/**
 * Summary description for SatGeneticOperator.
 */
public class SatGeneticOperator extends GeneticOperator
{
	public Subject mutate(Subject subject) throws Exception
	{
		try
		{
			int length = subject.getGenome().getChromosome(0).getLength();
			int count = _random.nextInt((int)(length * _mutatePct) + 1);
			Subject mutant = (Subject)subject.clone();

			for (int i = 0; i < count; i++)
			{
				int ix = _random.nextInt(length);
				int x = mutant.getGenome().getChromosome(0).getGene(ix).getValue();
				mutant.getGenome().getChromosome(0).getGene(ix).setValue(1 - x);
			}
			return mutant;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}

	public Subject randomize(Subject subject) throws Exception
	{
		try
		{
			int length = subject.getGenome().getChromosome(0).getLength();

			Subject random = (Subject)subject.clone();

			for (int i = 0; i < length * 10; i++)
			{
				int ix = _random.nextInt(length - 2) + 1;
				int x = random.getGenome().getChromosome(0).getGene(ix).getValue();
				random.getGenome().getChromosome(0).getGene(ix).setValue(1 - x);
				random.getGenome().getChromosome(0).getGene(ix + 1).setValue(1 - x);
				random.getGenome().getChromosome(0).getGene(ix - 1).setValue(1 - x);
				random.getGenome().getChromosome(0).getGene(length - ix - 1).setValue(1 - x);
				random.getGenome().getChromosome(0).getGene(length - ix).setValue(1 - x);
				random.getGenome().getChromosome(0).getGene(length - ix - 2).setValue(1 - x);
			}
			return random;
		}
		catch (Exception ex)
		{
			throw ex;
		}
	}
}
