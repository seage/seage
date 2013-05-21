package org.seage.problem.rosenbrock.genetics;

import org.seage.metaheuristic.genetics.SubjectEvaluator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.rosenbrock.RosenbrockFunction;

public class RosenbrockEvaluator extends SubjectEvaluator<Double>
{
	@Override
	protected double[] evaluate(Subject<Double> solution) throws Exception
	{
		double[] vector = new double[solution.getChromosome().getLength()];
		for(int i=0;i<vector.length;i++)
			vector[i] = (Double)solution.getChromosome().getGene(i);
		return new double[]{RosenbrockFunction.f(vector)};
	}

}
