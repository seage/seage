package org.seage.metaheuristic.genetics;

/**
 * Genetic algorithm interface
 */
public interface Evaluator
{
	double[] evaluate(Subject solution) throws Exception;
}
