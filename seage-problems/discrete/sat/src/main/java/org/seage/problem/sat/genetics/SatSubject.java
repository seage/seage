package sat.algorithm.genetics;

import ailibrary.algorithm.genetics.*;
/**
 * Summary description for SatGSSubject.
 */
public class SatSubject extends Subject
{
	public SatSubject(Genome genome)
	{
		super(genome);
	}

	public SatSubject(Subject subject)
	{
		super(subject);
	}

	public Object clone()
	{
		SatSubject result = (SatSubject)super.clone();
		return result;
	}
		
	public String toString()
	{
		String str = "";
		Chromosome chrom = getGenome().getChromosome(0);
		for (int i = 0; i < chrom.getLength(); i++)
		{
			str += chrom.getGene(i).getValue();
		}
		return super.toString() + "\t" + str;
	}
}
