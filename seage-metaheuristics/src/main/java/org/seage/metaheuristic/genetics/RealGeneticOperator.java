package org.seage.metaheuristic.genetics;

import java.util.ArrayList;
import java.util.List;

import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.Subject;

public class RealGeneticOperator extends GeneticOperator<Double>	// private double _crossLengthPct;
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
	
	public RealGeneticOperator(Limit[] limits)
	{
		_limits = limits;
	}

	//@Override
	public Subject<Double>[] crossOver0(Subject<Double> parent1, Subject<Double> parent2) throws Exception
	{
		double crossFactor = _crossLengthPct /100.0;
		
		@SuppressWarnings("unchecked")
		Subject<Double>[] children = (Subject<Double>[])new Object[2];
		
		children[0] = parent1.clone();
		children[1] = parent2.clone();
		
		for(Subject<Double> s : children)
		{
			for(int i=0;i<parent1.getChromosome().getLength();i++)
			{
				if(crossFactor > _random.nextDouble())
				{
					double value = ((Double)parent1.getChromosome().getGene(i) + (Double)parent2.getChromosome().getGene(i))/2;
					s.getChromosome().setGene(i, value);
				}				
			}
		}
		return children;
	}

	//@Override
	public Subject<Double> mutate(Subject<Double>  subject) throws Exception
	{
		double mutateFactor = _mutateLengthPct /100.0;
	
		for(int i=0;i<subject.getChromosome().getLength();i++)
		{
			if(mutateFactor > _random.nextDouble())
			{
				double value = _limits[i].Min + _random.nextDouble()*(_limits[i].Max-_limits[i].Min);
				subject.getChromosome().setGene(i, value);
			}
		}
		return subject;
	}

	@Override
	public Subject<Double> randomize(Subject<Double>  subject) throws Exception
	{
		for(int i=0;i<subject.getChromosome().getLength();i++)
		{			
			double value = _limits[i].Min + _random.nextDouble()*(_limits[i].Max-_limits[i].Min);
			subject.getChromosome().setGene(i, value);			
		}
		return subject;
	}
	
}
