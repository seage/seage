package org.seage.problem.rosenbrock.genetics;

import java.util.ArrayList;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.RealGeneticOperator;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.rosenbrock.RosenbrockFunction;

public class RosenbrockGeneticAlgorithmTest
{
	public static void main(String[] args)
	{
		try
		{
			int dim = 11;

			double test[] = new double[dim];
			for (int i = 0; i < dim; i++)
				test[i] = 1;
			System.out.println(RosenbrockFunction.f(test));

			RealGeneticOperator.Limit[] limits = new RealGeneticOperator.Limit[dim];
			RealGeneticOperator operator = new RealGeneticOperator(limits);

			for (int i = 0; i < dim; i++)
				limits[i] = operator.new Limit(-10, 10);

			GeneticAlgorithm<Double> gs = new GeneticAlgorithm<Double>(operator, new RosenbrockEvaluator());
			gs.addGeneticSearchListener(new IAlgorithmListener<GeneticAlgorithmEvent<Double>>()
			{
				
				@Override
				public void noChangeInValueIterationMade(GeneticAlgorithmEvent<Double> e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void newBestSolutionFound(GeneticAlgorithmEvent<Double> e)
				{
					System.out.println(e.getGeneticSearch().getBestSubject().getObjectiveValue()[0]);
					
				}
				
				@Override
				public void iterationPerformed(GeneticAlgorithmEvent<Double> e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void algorithmStopped(GeneticAlgorithmEvent<Double> e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void algorithmStarted(GeneticAlgorithmEvent<Double> e)
				{
					// TODO Auto-generated method stub
					
				}
			});
			gs.setCrossLengthPct(0.4);
			gs.setEliteSubjectsPct(0.1);
			gs.setIterationToGo(10000);
			gs.setMutateChromosomeLengthPct(0.2);
			gs.setMutatePopulationPct(0.0);			
			gs.setPopulationCount(1000);
			gs.setRandomSubjectsPct(0.01);

			Double[] dimArray = new Double[dim];
			for(int i=0;i<dim;i++)
				dimArray[i] = new Double(0);
			
			ArrayList<Subject<Double>> subjects = new ArrayList<Subject<Double>>();
			for (int i = 0; i < gs.getPopulationCount(); i++)
			{
				subjects.add( new Subject<Double>(dimArray));
				operator.randomize(subjects.get(i));
			}

			gs.startSearching(subjects);

			System.out.println(gs.getBestSubject().getObjectiveValue()[0]);
			for (int i = 0; i < dim; i++)
				System.out.print(gs.getBestSubject().getChromosome().getGene(i) + " ");
			System.out.println();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
