package org.seage.problem.rosenbrock.genetics;

import org.seage.metaheuristic.genetics.Evaluator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.rosenbrock.RosenbrockFunction;

public class RosenbrockEvaluator implements Evaluator
{

	@Override
	public double[] evaluate(Subject solution) throws Exception
	{
		double[] vector = new double[solution.getGenome().getChromosome(0).getLength()];
		for(int i=0;i<vector.length;i++)
			vector[i] = (Double)solution.getGenome().getChromosome(0).getGene(i).getValue();
		return new double[]{RosenbrockFunction.f(vector)};
	}

}
