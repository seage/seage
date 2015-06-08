package org.seage.problem.sat.genetics;

import org.seage.metaheuristic.genetics.Subject;
/**
 * Summary description for SatGSSubject.
 */
public class SatSubject extends Subject<Boolean>
{

	public SatSubject(Boolean[] geneValues) {
		super(geneValues);
	}
//
//	public SatSubject(Subject subject)
//	{
//		super(subject);
//	}
//
//	public Object clone()
//	{
//		SatSubject result = (SatSubject)super.clone();
//		return result;
//	}
//		
//	public String toString()
//	{
//		String str = "";
//		Chromosome chrom = getGenome().getChromosome(0);
//		for (int i = 0; i < chrom.getLength(); i++)
//		{
//			str += chrom.getGene(i).getValue();
//		}
//		return super.toString() + "\t" + str;
//	}
}
