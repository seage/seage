package org.seage.experimenter.singlealgorithm.evolution;

import java.util.List;

import org.seage.metaheuristic.genetics.GeneticOperator;
import org.seage.metaheuristic.genetics.RealGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;

public class ExperimentGeneticOperator extends RealGeneticOperator
{

	public ExperimentGeneticOperator(Limit[] limits)
	{
		super(limits);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Subject<Double>> crossOver0(Subject<Double> parent1, Subject<Double> parent2) throws Exception
	{
		// TODO Auto-generated method stub
		return super.crossOver0(parent1, parent2);
	}

	@Override
	public Subject<Double> mutate(Subject<Double> subject) throws Exception
	{
		// TODO Auto-generated method stub
		return super.mutate(subject);
	}

	@Override
	public Subject<Double> randomize(Subject<Double> subject) throws Exception
	{
		// TODO Auto-generated method stub
		return super.randomize(subject);
	}

	
	
}
