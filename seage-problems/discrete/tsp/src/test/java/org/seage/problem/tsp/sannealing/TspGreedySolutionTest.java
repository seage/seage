package org.seage.problem.tsp.sannealing;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.seage.aal.problem.IProblemProvider;
import org.seage.aal.problem.ProblemProvider;
import org.seage.problem.tsp.City;
import org.seage.problem.tsp.TspProblemInstance;
import org.seage.problem.tsp.tour.TspOptimalTour;
import org.seage.problem.tsp.tour.TspOptimalTourBerlin52;

public class TspGreedySolutionTest
{
	private TspOptimalTour _optimalTour;
	private City[] _cities;
	
	@Before
	public void setUp() throws Exception
	{
		_optimalTour =  new TspOptimalTourBerlin52();
		IProblemProvider provider = ProblemProvider.getProblemProviders().get("TSP");
		TspProblemInstance instance = (TspProblemInstance)provider.initProblemInstance(provider.getProblemInfo().getInstanceInfo(_optimalTour.Name));
		
		_cities = instance.getCities();
	}

	@Test
	public void testGetTour() throws Exception
	{		
		testGetTour(new TspRandomSolution(_cities.length));
		testGetTour(new TspSortedSolution(_cities.length));
		testGetTour(new TspGreedySolution(_cities));		
	}
	
	private void testGetTour(TspSolution solution)
	{
		assertEquals(_optimalTour.OptimalTour.length, solution.getTour().length);
		Arrays.sort(solution.getTour());
		assertNotSame(0, solution.getTour()[0]);
		assertEquals(1, (int)solution.getTour()[0]);
		assertEquals(_optimalTour.OptimalTour.length, (int)solution.getTour()[_optimalTour.OptimalTour.length-1]);
		
		for(int i=0;i<_optimalTour.OptimalTour.length-1;i++)
		{
			assertEquals((int)solution.getTour()[i], (int)solution.getTour()[i+1]-1);
		}
	}

}
