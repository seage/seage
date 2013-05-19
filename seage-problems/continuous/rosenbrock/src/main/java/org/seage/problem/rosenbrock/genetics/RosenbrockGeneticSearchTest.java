package org.seage.problem.rosenbrock.genetics;

import org.seage.metaheuristic.IAlgorithmListener;
import org.seage.metaheuristic.genetics.GeneticSearch;
import org.seage.metaheuristic.genetics.GeneticSearchEvent;
import org.seage.metaheuristic.genetics.Genome;
import org.seage.metaheuristic.genetics.Subject;
import org.seage.problem.rosenbrock.RosenbrockFunction;

public class RosenbrockGeneticSearchTest
{
	public static void main(String[] args)
	{
		try
		{
			int dim = 10;

			double test[] = new double[dim];
			for (int i = 0; i < dim; i++)
				test[i] = 1;
			System.out.println(RosenbrockFunction.f(test));

			RosenbrockGeneticOperator.Limit[] limits = new RosenbrockGeneticOperator.Limit[dim];
			RosenbrockGeneticOperator operator = new RosenbrockGeneticOperator(limits);

			for (int i = 0; i < dim; i++)
				limits[i] = operator.new Limit(-10, 10);

			GeneticSearch gs = new GeneticSearch(operator, new RosenbrockEvaluator());
			gs.addGeneticSearchListener(new IAlgorithmListener<GeneticSearchEvent>()
			{
				
				@Override
				public void noChangeInValueIterationMade(GeneticSearchEvent e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void newBestSolutionFound(GeneticSearchEvent e)
				{
					System.out.println(e.getGeneticSearch().getBestSubject().getObjectiveValue()[0]);
					
				}
				
				@Override
				public void iterationPerformed(GeneticSearchEvent e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void algorithmStopped(GeneticSearchEvent e)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void algorithmStarted(GeneticSearchEvent e)
				{
					// TODO Auto-generated method stub
					
				}
			});
			gs.setCrossLengthPct(0.3);
			gs.setEliteSubjectsPct(0.2);
			gs.setIterationToGo(200000);
			gs.setMutateChromosomeLengthPct(0.1);
			gs.setMutatePopulationPct(0.0);			
			gs.setPopulationCount(100);
			gs.setRandomSubjectsPct(0.01);

			Subject[] subjects = new Subject[100];
			for (int i = 0; i < subjects.length; i++)
			{
				subjects[i] = new Subject(new Genome(1, dim));

				operator.randomize(subjects[i]);

			}

			gs.startSearching(subjects);

			System.out.println(gs.getBestSubject().getObjectiveValue()[0]);
			for (int i = 0; i < dim; i++)
				System.out.print(gs.getBestSubject().getGenome().getChromosome(0).getGene(i).getValue() + " ");
			System.out.println();
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
