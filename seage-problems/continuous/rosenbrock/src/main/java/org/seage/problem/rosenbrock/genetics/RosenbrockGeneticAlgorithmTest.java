package org.seage.problem.rosenbrock.genetics;

import java.util.ArrayList;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticAlgorithm;
import org.seage.metaheuristic.genetics.GeneticAlgorithmEvent;
import org.seage.metaheuristic.genetics.ContinuousGeneticOperator;
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

			ContinuousGeneticOperator.Limit[] limits = new ContinuousGeneticOperator.Limit[dim];
			ContinuousGeneticOperator<Subject<Double>> operator = new ContinuousGeneticOperator<Subject<Double>>(limits);

			for (int i = 0; i < dim; i++)
				limits[i] = new  ContinuousGeneticOperator.Limit(-10, 10);

			GeneticAlgorithm<Subject<Double> > gs = new GeneticAlgorithm<Subject<Double> >(operator, new RosenbrockEvaluator());
			gs.addGeneticSearchListener(new IAlgorithmListener<GeneticAlgorithmEvent<Subject<Double> >>()
			{
				
				@Override
				public void noChangeInValueIterationMade(GeneticAlgorithmEvent<Subject<Double> > e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void newBestSolutionFound(GeneticAlgorithmEvent<Subject<Double> > e)
				{
					System.out.println(e.getGeneticSearch().getBestSubject().getObjectiveValue()[0]);
					
				}
				
				@Override
				public void iterationPerformed(GeneticAlgorithmEvent<Subject<Double> > e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void algorithmStopped(GeneticAlgorithmEvent<Subject<Double> > e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void algorithmStarted(GeneticAlgorithmEvent<Subject<Double> > e)
				{
					// TODO Auto-generated method stub
					
				}
			});
			gs.setCrossLengthPct(40);
			gs.setEliteSubjectsPct(10);
			gs.setIterationToGo(10000);
			gs.setMutateChromosomeLengthPct(20);
			gs.setMutatePopulationPct(10);			
			gs.setPopulationCount(1000);
			gs.setRandomSubjectsPct(1);

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
			log.error("{}", ex.getMessage(), ex);
		}
	}
}
