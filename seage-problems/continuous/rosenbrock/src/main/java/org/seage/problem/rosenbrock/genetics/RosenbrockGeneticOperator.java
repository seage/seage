package org.seage.problem.rosenbrock.genetics;

import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.Subject;

public class RosenbrockGeneticOperator extends GeneticOperator
{
	private Limit[] _limits;
	
	public class Limit
	{
		public double Min=0;
		public double Max=0;
		public Limit(double min, double max)
		{
			Min = min;
			Max = max;
		}
	}
	
	public RosenbrockGeneticOperator(Limit[] limits)
	{
		_limits = limits;
	}

	//@Override
	public Subject[] crossOver(Subject parent1, Subject parent2) throws Exception
	{
		double crossFactor = _crossLengthPct /100.0;
		
		Subject[] children = new Subject[2];
		children[0] = (Subject)parent1.clone();
		children[1] = (Subject)parent2.clone();
		
		for(Subject s : children)
		{
			for(int i=0;i<parent1.getGenome().getChromosome(0).getLength();i++)
			{
				if(crossFactor > _random.nextDouble())
				{
					double value = ((Double)parent1.getGenome().getChromosome(0).getGene(i).getValue() + (Double)parent2.getGenome().getChromosome(0).getGene(i).getValue())/2;
					s.getGenome().getChromosome(0).getGene(i).setValue(value);
				}				
			}
		}
		return children;
	}

	@Override
	public Subject mutate(Subject subject) throws Exception
	{
		double mutateFactor = _mutateLengthPct /100.0;
	
		for(int i=0;i<subject.getGenome().getChromosome(0).getLength();i++)
		{
			if(mutateFactor > _random.nextDouble())
			{
				double value = _limits[i].Min + _random.nextDouble()*(_limits[i].Max-_limits[i].Min);
				subject.getGenome().getChromosome(0).getGene(i).setValue(value);
			}
		}
		return subject;
	}

	@Override
	public Subject randomize(Subject subject) throws Exception
	{
		for(int i=0;i<subject.getGenome().getChromosome(0).getLength();i++)
		{			
			double value = _limits[i].Min + _random.nextDouble()*(_limits[i].Max-_limits[i].Min);
			subject.getGenome().getChromosome(0).getGene(i).setValue(value);
			
		}
		return subject;
	}
	
}
